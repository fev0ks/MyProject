package scienceWork.dataBase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.SVM;
import scienceWork.objects.GeneralPicturesInformation;
import scienceWork.objects.constants.Settings;
import scienceWork.objects.machineLearning.CommonML.AlgorithmML;
import scienceWork.objects.data.BOWVocabulary;
import scienceWork.objects.data.Vocabulary;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkerDB {
    private static volatile WorkerDB instance;
    private ConnectorDB connectorDB;


    private WorkerDB() throws SQLException, ClassNotFoundException {
        connectorDB = new ConnectorDB();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static WorkerDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            synchronized (ManagerDB.class) {
                if (instance == null)
                    instance = new WorkerDB();
            }
        return instance;
    }


    public boolean saveVocabulary(Vocabulary vocabulary) {
        String picToDBQuery = " insert into vocabulary(clusters, size, used_images, used_descr, date, mat_id) values(?,?,?,?,?,?)";
        long matId = saveMat(vocabulary.getMat(), vocabulary.getFeatureId());
        boolean saved = false;
        Timestamp date = new java.sql.Timestamp(vocabulary.getCreatedDate());
        if (!BOWVocabulary.vocabulary.getMat().empty()) {
            try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(picToDBQuery)) {
                preparedStatement.setInt(1, vocabulary.getCountClusters());
                preparedStatement.setInt(2, vocabulary.getSize());
                preparedStatement.setInt(3, vocabulary.getPicCounts());
                preparedStatement.setLong(4, vocabulary.getDescriptors());
                preparedStatement.setTimestamp(5, date);
                preparedStatement.setLong(6, matId);
                preparedStatement.execute();
                saved = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return saved;
    }

    private long saveMat(Mat mat, int featureId) {
        String saveMat = "insert into mat(rows, cols, cv_type, mat, feature_id) values(?,?,?,?,?)";
        long id = -1;
        try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(saveMat, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, mat.rows());
            preparedStatement.setInt(2, mat.cols());
            preparedStatement.setInt(3, mat.type());
            preparedStatement.setBytes(4, convertMatToBytes(mat));
            preparedStatement.setInt(5, featureId);
            preparedStatement.execute();
            id = getGeneratedId(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<Vocabulary> loadVocabulary(int size, int featureId) throws SQLException {
        String loadVocabulary = "select \n" +
                "v.clusters, v.date, v.used_descr, m.rows, m.cols, m.cv_type, v.used_images, m.mat " +
                "from vocabulary v " +
                "left join mat m " +
                "on v.mat_id=m.id " +
                "left join feature f " +
                "on m.feature_id = f.id " +
                "where " +
                "v.size = ? " +
                "and m.feature_id = ?";
        int countClusters = 0;
        int rows = 0;
        int cols = 0;
        int cvType = 0;
        int picCount = 0;
        long descriptors = 0;
        byte[] bytes = null;
        Timestamp date = null;

        List<Vocabulary> vocabularies = new ArrayList<>();
        PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(loadVocabulary);
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, featureId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                countClusters = resultSet.getInt(1);
                date = resultSet.getTimestamp(2);
                descriptors = resultSet.getLong(3);
                rows = resultSet.getInt(4);
                cols = resultSet.getInt(5);
                cvType = resultSet.getInt(6);
                picCount = resultSet.getInt(7);
                bytes = resultSet.getBytes(8);



                Mat mat = convertBytesToMat(rows, cols, cvType, bytes);
                Vocabulary vocabulary = new Vocabulary(rows, cols, cvType, featureId, size, picCount, descriptors, date.getTime(), mat, countClusters);

                vocabularies.add(vocabulary);

            }
        preparedStatement.close();
        System.out.println("Load vocabularies: " + vocabularies.size());
        return vocabularies;
    }

    public boolean saveClassifier(AlgorithmML classifier) {

        String saveClassifier = " insert into classifier_data(clusters, settings, date, path_data, classifier_id) values(?,?,?,?,?)";
        boolean saved = false;

        Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
        String path = saveClassifierData(classifier);
        if(path!=null) {
            JSONArray classes = new JSONArray(BOWVocabulary.getClustersTypes());
            try (PreparedStatement preparedStatement = connectorDB.getConnection().prepareStatement(saveClassifier)) {
                preparedStatement.setString(1, classes.toString());
                preparedStatement.setString(2, "");
                preparedStatement.setTimestamp(3, date);
                preparedStatement.setString(4, path);
                preparedStatement.setInt(5, classifier.getClassifierId());
                preparedStatement.execute();
                saved = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return saved;
    }

    private String saveClassifierData(AlgorithmML classifier) {
        if (classifier.getInstance() instanceof SVM) {
            SVM svm = (SVM) classifier.getInstance();
            long time = Calendar.getInstance().getTimeInMillis();
            String s = "E:\\YandexDisk\\JavaProject\\SDiplom\\classifiers\\"+classifier.getType()+"_method "+Settings.getMethod()+"_words "+Settings.getCountWords()+"_clusters "+BOWVocabulary.vocabularies.size()+"_usedPic "+GeneralPicturesInformation.getInstance().getPictureCount()+"_"+time+".xml";
            svm.save(s);
            if(svm.load(s).isClassifier())
            return s;
        }
        return null;
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
                throw new SQLException("no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
