package scienceWork.algorithms.Interfaces;

import scienceWork.objects.Picture;

import java.util.List;

public interface IImageWorker {

    void getImagesType(List<Picture> pictList);

    void findFeaturesForImages(List<Picture> pictList);
}
