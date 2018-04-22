package scienceWork.dataBase;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import scienceWork.objects.picTypesData.BOWVocabulary;

import java.sql.*;
import java.util.Calendar;

public class VocabularyDB {
    private static volatile VocabularyDB instance;
    private ConnectorDB connectorDB;

    private VocabularyDB() {
        connectorDB = new ConnectorDB();
    }

    public static VocabularyDB getInstance() {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new VocabularyDB();
            }
        return instance;
    }


    public boolean saveVocabulary(int countClusters, int countImages, int featureTypeId, int size, Mat mat) {
        String picToDBQuery = " insert into vocabulary(count_clasters, count_images, size, date, id_type, id_mat) values(?,?,?,?,?,?)";
        long matId = saveMat(mat);

        Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
        if (!BOWVocabulary.commonVocabulary.empty()) {
            try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery)) {
                preparedStatement.setInt(1, countClusters);
                preparedStatement.setInt(2, countImages);
                preparedStatement.setInt(3, size);
                preparedStatement.setTimestamp(4, date);
                preparedStatement.setLong(5, featureTypeId);
                preparedStatement.setLong(6, matId);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    private long saveMat(Mat mat) {
        String saveMat = "insert into mat_info(total, rows, cols, cv_type, mat) values(?,?,?,?,?)";
        long id = -1;
        try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(saveMat, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, mat.total());
            preparedStatement.setInt(2, mat.rows());
            preparedStatement.setInt(3, mat.cols());
            preparedStatement.setInt(4, mat.type());
            preparedStatement.setBytes(5, convertMatToBytes(mat));
            preparedStatement.execute();
            id = getGeneratedId(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Mat loadVocabulary(int size, int featureType){
        String loadVocabulary = "select \n" +
                "m.total, m.rows, m.cols, m.cv_type, m.mat " +
                "from vocabulary v " +
                "left join mat_info m " +
                "on v.id_mat=m.id " +
                "left join feature f " +
                "on v.id_type = f.id " +
                "where " +
                "v.size = ? " +
                "and v.id_type = ?";
        int total=0;
        int rows=0;
        int cols=0;
        int cvType=0;
        byte[] bytes= null;
        try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(loadVocabulary)) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, featureType);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getInt(1);
                rows = resultSet.getInt(2);
                cols = resultSet.getInt(3);
                cvType = resultSet.getInt(4);
                bytes = resultSet.getBytes(5);
            } else {
                throw new SQLException("Failed to load Mat");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return convertBytesToMat(rows, cols, cvType, bytes);
    }

    private byte[] convertMatToBytes(Mat mat) {
        long nbytes = mat.total() * mat.elemSize();
        byte[] bytes = new byte[(int) nbytes];
        mat.convertTo(mat, CvType.CV_8U);
        mat.get(0, 0, bytes);
        return bytes;
    }

    private Mat convertBytesToMat(int rows, int cols, int type, byte[] bytes) {
        Mat mat = new Mat(rows, cols, type);
        mat.put(0, 0, bytes);
        mat.convertTo(mat, type);
        return mat;

    }

    private long getGeneratedId(PreparedStatement statement) {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return (generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
