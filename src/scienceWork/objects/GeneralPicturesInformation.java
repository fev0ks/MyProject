package scienceWork.objects;

public class GeneralPicturesInformation {
    private int pictureCount;
    private double sumHeight;
    private double sumWidth;

    private int countKeyPoints;

//    private int lengthDescr;

    private int minKeyPoints;
    private int maxKeyPoints;

    private static GeneralPicturesInformation generalPictureInformation;

    public static GeneralPicturesInformation getInstance(){
        if(generalPictureInformation == null) {
            generalPictureInformation = new GeneralPicturesInformation();
        }

        return generalPictureInformation;
    }

    private GeneralPicturesInformation(){
//        lengthDescr = 0;
        countKeyPoints = 0;
        sumHeight = 0;
        sumWidth = 0;
        pictureCount = 0;
        minKeyPoints = Integer.MAX_VALUE;
        maxKeyPoints = 0;
    }

//    public int getLengthDescr() {
//        return lengthDescr;
//    }

//    public void setLengthDescr(int lengthDescr) {
//        this.lengthDescr = lengthDescr;
//    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

    public double getSumHeight() {
        return sumHeight;
    }

    public void setSumHeight(double sumHeight) {
        this.sumHeight = sumHeight;
    }

    public double getAvrgWidth() {
        return sumWidth;
    }

    public void setAvrgWidth(double avrgWidth) {
        this.sumWidth = avrgWidth;
    }

    public int getCountKeyPoints() {
        return countKeyPoints;
    }

    public void setCountKeyPoints(int countKeyPoints) {
        this.countKeyPoints = countKeyPoints;
    }

    public int getMinKeyPoints() {
        return minKeyPoints;
    }

    public void setMinKeyPoints(int minKeyPoints) {
        this.minKeyPoints = minKeyPoints;
    }

    public int getMaxKeyPoints() {
        return maxKeyPoints;
    }

    public void setMaxKeyPoints(int maxKeyPoints) {
        this.maxKeyPoints = maxKeyPoints;
    }

    public void updateGeneralInformation(Picture picture){
        if(picture.getDescriptorProperty() != null) {

            int countKeyPoints = picture.getDescriptorProperty().getCountOfKP();
            this.countKeyPoints += countKeyPoints;
            sumHeight = picture.getDimension().height;
            sumWidth = picture.getDimension().width;

            if(this.maxKeyPoints < countKeyPoints){
                this.maxKeyPoints = countKeyPoints;
            }
            if(this.minKeyPoints > countKeyPoints){
                this.minKeyPoints = countKeyPoints;
            }
//            if(lengthDescr == 0){
//                this.lengthDescr = picture.getDescriptorProperty().getMatOfDescription().cols();
//            }
        }
    }

    public void clearKPData(){
        maxKeyPoints = 0;
        minKeyPoints = Integer.MAX_VALUE;
        countKeyPoints = 0;
//        lengthDescr = 0;
    }
}
