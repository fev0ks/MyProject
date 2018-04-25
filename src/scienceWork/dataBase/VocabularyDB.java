package scienceWork.dataBase;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VocabularyDB {
    private static volatile VocabularyDB instance;
    private ConnectorDB connectorDB;


    private VocabularyDB() {
        connectorDB = new ConnectorDB();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static VocabularyDB getInstance() {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new VocabularyDB();
            }
        return instance;
    }


    public boolean saveVocabulary(int countClusters, Vocabulary vocabulary) {
        String picToDBQuery = " insert into vocabulary(count_clasters, count_images, size, descriptors, id_type, id_mat, date) values(?,?,?,?,?,?,?)";
        long matId = saveMat(vocabulary.getVocabulary());

        Timestamp date = new java.sql.Timestamp(vocabulary.getCreatedDate());
        if (!BOWVocabulary.vocabulary.getVocabulary().empty()) {
            try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery)) {
                preparedStatement.setInt(1, countClusters);
                preparedStatement.setInt(2, vocabulary.getPicCounts());
                preparedStatement.setInt(3, vocabulary.getSize());
                preparedStatement.setLong(4, vocabulary.getDescriptors());
                preparedStatement.setLong(5, vocabulary.getFeatureId());
                preparedStatement.setLong(6, matId);
                preparedStatement.setTimestamp(7, date);
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

    public List<Vocabulary> loadVocabulary(int size, int featureId){
        String loadVocabulary = "select \n" +
                "m.total, m.rows, m.cols, m.cv_type, v.count_images, v.date, v.descriptors, m.mat " +
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
        int picCount=0;
        long descriptors = 0;
        byte[] bytes= null;
        Timestamp date = null;
        List<Vocabulary> vocabularies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(loadVocabulary)) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, featureId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                total = resultSet.getInt(1);
                rows = resultSet.getInt(2);
                cvType = resultSet.getInt(3);
                cvType = resultSet.getInt(4);
                picCount = resultSet.getInt(5);
                date = resultSet.getTimestamp(6);
                descriptors = resultSet.getLong(7);
                bytes = resultSet.getBytes(8);
                Mat mat = convertBytesToMat(rows, cols, cvType, bytes);
                Vocabulary vocabulary = new Vocabulary(total, rows, cols, cvType, featureId, size, picCount, descriptors, date.getTime(), mat);

                vocabularies.add(vocabulary);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
       return vocabularies;
    }
    private byte[] convertMatToBytes(Mat mat) {
        long nbytes = mat.total() * mat.elemSize();
        byte[] bytes = new byte[(int) nbytes];
        Mat newMat = new Mat();
        mat.convertTo(newMat, CvType.CV_8U);
        newMat.get(0, 0, bytes);
        return bytes;
    }

    private Mat convertBytesToMat(int rows, int cols, int type, byte[] bytes) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(rows, cols, CvType.CV_8U);
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
