package scienceWork.algorithms;


import org.opencv.core.Core;
import scienceWork.FxWorker.Interfaces.Progress;
import scienceWork.algorithms.DescriptorProcess.ClusterTools;

public class ClusteringThroughDescriptionClusters {
//    private List<Picture> pictList;
    private Progress progress;
    private String typeImages;
    private ClusterTools clusterTools;


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public ClusteringThroughDescriptionClusters(String typeImages, Progress progress) {
        this.typeImages = typeImages;
        this.progress = progress;
        clusterTools= new ClusterTools();
    }

    public ClusteringThroughDescriptionClusters( Progress progress) {
        this.progress = progress;
    }

//    //По известным кластерам определяю типы ихображений
//    public void findPictureType(List<Picture> pictList) {
//        clusterTools.findPicturesClusters(pictList,progress);
//        Map<String, Mat> allTypeClusters = ImgTypesClusters.addGeneralizedClustersForInputTypeImage;
//        for (Picture picture : pictList) {
//            Mat clustersOfPicture = picture.getDescriptorProperty().getCentersOfDescriptors();
//            double minDistance = Double.MAX_VALUE;
//            double distance;
//            String bestGroup = "";
//            for (int i = 0; i < clustersOfPicture.height(); i++) {
//                for (Map.Entry<String, Mat> templateCluster : allTypeClusters.entrySet()) {
//                    for (int j = 0; j < templateCluster.getValue().height(); j++) {
//                        distance = clusterTools.findDistanceForMats(clustersOfPicture, templateCluster.getValue().row(j));
////                        System.out.println(picture.getName() + " " + i + " - " + j + " " + distance);
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            bestGroup = templateCluster.getKey();
//                        }
//                    }
//                }
//            }
//            picture.setPictureType(bestGroup);
////            System.out.println(picture.getName() + " " + bestGroup + " " + minDistance);
//        }
//    }



//    private Mat filteringClusters(Mat commonClusters) {
//        System.out.println("commonClusters.width() " + commonClusters.width());
//        Map<Integer, Double> ratingClusters = initRatingClustersMap(commonClusters.height());
//        for (Picture picture : pictList) {
//            System.out.println("pic height " + picture.getDescriptorProperty().getCentersOfDescriptors().height());
//            for (int i = 0; i < picture.getDescriptorProperty().getCentersOfDescriptors().height(); i++) {
//                double minDistance = Double.MAX_VALUE;
//                double distance;
//                int minRowCluster = -1;
//                Mat clusterOfOwnPicture = picture.getDescriptorProperty().getCentersOfDescriptors().row(i);
//                for (int j = 0; j < commonClusters.height(); j++) {
//                    distance = clusterTools.findDistanceForMats(clusterOfOwnPicture, commonClusters.row(j));
//                    System.out.println(picture.getName() + " " + i + " - " + j + " " + distance);
//                    if (distance < minDistance) {
//                        minDistance = distance;
//                        minRowCluster = j;
//                    }
//                }
//                ratingClusters.put(minRowCluster, ratingClusters.get(minRowCluster) + minDistance);
//
//                System.out.println(picture.getName() + " " + i + " " + minDistance);
//
//            }
//        }
//        System.out.println("ratingClusters " + ratingClusters);
//
//        return selectBestClusters(ratingClusters, commonClusters);
//    }

//    private Map<Integer, Double> initRatingClustersMap(int size) {
//        Map<Integer, Double> map = new HashMap<>();
//        for (int i = 0; i < size; i++) {
//            map.put(i, -1D);
//        }
//        return map;
//    }
//
//    private Mat selectBestClusters(Map<Integer, Double> ratingClusters, Mat commonClusters) {
//        double avrgDistance = 0;
//
//        for (Map.Entry<Integer, Double> entry : ratingClusters.entrySet()) {
//            avrgDistance += entry.getValue();
//        }
//        avrgDistance = avrgDistance / ratingClusters.size();
//        System.out.println("avrgDistance " + avrgDistance);
//
//        Map<Integer, Double> numbersOfBestClusters = new HashMap<>();
//        Mat bestClusters = new Mat();
//        for (Map.Entry<Integer, Double> entry : ratingClusters.entrySet()) {
//            if (entry.getValue() < avrgDistance) {
//                numbersOfBestClusters.put(entry.getKey(), entry.getValue());
//                bestClusters.push_back(commonClusters.row(entry.getKey()));
//            }
//        }
//
//        System.out.println("numbersOfBestClusters " + numbersOfBestClusters);
//        System.out.println("bestClusters " + bestClusters);
//        return bestClusters;
//    }

//    private double findDistanceForMats(Mat mat1, Mat mat2) {
//        double distance = 0;
//        if (mat1.width() != mat2.width()) return Double.MAX_VALUE;
//        for (int i = 0; i < mat1.width(); i++) {
//            distance += Math.abs(mat1.get(0, i)[0] - mat2.get(0, i)[0]);
//        }
//        return distance;
//    }

//    private void printMat(Mat mat) {
//        System.out.println("Mat[ ");
//        for (int i = 0; i < mat.height(); i++) {
//            for (int j = 0; j < mat.width(); j++) {
//                System.out.print(Arrays.toString(mat.get(i, j)) + " ");
//            }
//            System.out.println();
//        }
//        System.out.print("]");
//    }

//    private int findDistance(List<Integer> descr1, List<Integer> descr2) {
//        int range = 0;
//        for (int i = 0; i < descr1.size(); i++) {
//            range += Math.abs(descr1.get(i) - descr2.get(i));
//        }
//        return range;
//    }
//
//    public List<GroupDescr> findGroups(List<GroupDescr> descrList) {
//        int groupCount = 0;
//        double avrDistance = 0;
//        int leastDistance = Integer.MAX_VALUE;
//        int biggestDistance = Integer.MIN_VALUE;
//        int count = 0;
//        for (int i = 0; i < descrList.size() - 1; i++) {
//            progressBar.setProgress((double) (i) / (descrList.size()));
//            int minDistance = Integer.MAX_VALUE;
//            int minDistanceNumber = 0;
//            int distance;
//            for (int j = i + 1; j < descrList.size(); j++) {
//
//                if (descrList.get(i).getPictureName().equals(descrList.get(j).getPictureName())) {
//                    distance = findDistance(descrList.get(i).getDescr(), descrList.get(j).getDescr());
//                    if (distance < 4000 && distance < minDistance) {
//                        minDistance = distance;
////                        System.out.print(distance+" ");
//                        minDistanceNumber = j;
//                        count++;
//                        avrDistance += distance;
//                        if (distance < leastDistance) leastDistance = distance;
//                        if (distance > biggestDistance) biggestDistance = distance;
//                    }
//                }
//
//            }
//            if (descrList.get(i).getPictureType() == 0) {
//                if (descrList.get(minDistanceNumber).getPictureType() == 0) {
//                    descrList.get(i).setPictureType(groupCount);
//                    descrList.get(minDistanceNumber).setPictureType(groupCount);
//                    groupCount++;
//                } else {
//                    descrList.get(i).setPictureType(descrList.get(minDistanceNumber).getPictureType());
//                }
//            } else {
//                if (descrList.get(minDistanceNumber).getPictureType() == 0) {
//                    descrList.get(minDistanceNumber).setPictureType(descrList.get(i).getPictureType());
//                }
//            }
//        }
//        System.out.println("avrDistance= " + avrDistance / count +
//                "\n leastDist= " + leastDistance +
//                "\n biggestDist= " + biggestDistance
//        );
//        return descrList;
//    }
}
