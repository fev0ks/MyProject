package scienceWork.dataBase;

import javafx.collections.ObservableList;
import scienceWork.ObjectClasses.Picture;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDB {
    private static volatile ManagerDB instance;
    private ConnectorDB connectorDB;

    private ManagerDB() {
        connectorDB = new ConnectorDB();
    }

    public static ManagerDB getInstance() {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new ManagerDB();
            }
        return instance;
    }

//TODO
    public String picToDBfromDir(ObservableList<Picture> picturesList) {
        String picToDBQuery = " insert into photo(name,size,id_folder) values(?,?,?) ";

        String message = "Загружено.";
        int idFolder = getID(picturesList.get(0).getDir(),"folder");
       // System.out.println(picturesList.get(0).getDir());
        try {
            for (Picture picture : picturesList) {
              //  System.out.println(picture.getName() + " idFolder:" + idFolder);
                PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery);
              //  preparedStatement.setString(4, picture.getName()); //хотел проверку сделать, если уже существует
                preparedStatement.setString(1, picture.getName());
                preparedStatement.setDouble(2, picture.getSize());
                preparedStatement.setInt(3, idFolder);
                preparedStatement.execute();
                preparedStatement.close();
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return message;
    }
//    IF EXISTS (SELECT * FROM table_name WHERE id = ?)
//    BEGIN
//--do what needs to be done if exists
//            END
//    ELSE
//            BEGIN
//--do what needs to be done if not
//            END
    private int getID(String name, String type) {
        String getIdQuery = "select id_"+type+" from "+type+" where name=?;";
        PreparedStatement preparedStatement;
        int id = 0;
        try {
            preparedStatement = connectorDB.getConnection().prepareStatement(getIdQuery);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                id = resultSet.getInt(1);
          //  System.out.println("ID:" + id);
            preparedStatement.close();
        } catch (SQLException e) {
          //  e.printStackTrace();
        }
        return id;
    }

    public String dirToDB(File file) {

        String message = "";
        if(file==null) return message="Ошибка файла!";
        PreparedStatement preparedStatement;
        int countFiles = isDirExistenceInDB(file);
        if (countFiles == -1) {
            String picToDBQuery = "insert into folder(name,count_files) values(?,?);";
            try {
                preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery);
                preparedStatement.setString(1, file.getAbsolutePath());
                preparedStatement.setInt(2, file.listFiles().length);
                preparedStatement.execute();
                message = "Дирректория добавлена: " + file.listFiles().length + ". ";
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (countFiles != file.listFiles().length) {
                String picToDBQuery = "update folder set count_files=? where name=?;";
                try {
                    preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery);
                    preparedStatement.setInt(1, file.listFiles().length);
                    preparedStatement.setString(2, file.getAbsolutePath());
                    preparedStatement.execute();
                    message = "Количество файлов изменено " + countFiles + "-->" + file.listFiles().length + ". ";
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return message;
    }

    private int isDirExistenceInDB(File file) {
        String picToDBQuery = "select count_files from folder where name=?;";
        boolean dirExist = false;
        int countFiles = -1;
        try {
            PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery);
            preparedStatement.setString(1, file.getAbsolutePath());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                countFiles = resultSet.getInt(1);
                dirExist = true;
            }
            preparedStatement.close();
            System.out.println("dirExist " + dirExist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countFiles;
    }

    public void setPicDimensionsDB(Picture picture) {
        String setDimQuery = "insert into dimensions values(?,?,?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connectorDB.getConnection().prepareStatement(setDimQuery);
            preparedStatement.setInt(1, getID(picture.getName(),"photo"));
            preparedStatement.setInt(2, picture.getDimension().width);
            preparedStatement.setInt(3, picture.getDimension().height);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
           // e.printStackTrace();
        }
    }
}
