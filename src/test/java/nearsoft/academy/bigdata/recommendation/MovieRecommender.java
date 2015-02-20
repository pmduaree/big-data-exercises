package nearsoft.academy.bigdata.recommendation;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;

public  class MovieRecommender {
    //global variables
    public DataModel model ;
    public UserBasedRecommender recommender;
    public UserSimilarity similarity;
    public UserNeighborhood neighborhood;
    public Recommender cachingRecommender;
    public TextReducer textReducer;
    private MemoryIDMigrator memoryIDMigrator;
    //constructor
    public MovieRecommender(String path)throws IOException,TasteException{    

        //cleans the file using the text reducer class
        memoryIDMigrator = new MemoryIDMigrator();
        textReducer = new TextReducer(path, memoryIDMigrator);
        textReducer.clean();

        //use the library to create the recommender system
        model = new FileDataModel(new File("temp.csv"));
        similarity = new PearsonCorrelationSimilarity(model);
        neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        cachingRecommender = new CachingRecommender(recommender);


    }
    public int getTotalReviews()
    {
        return textReducer.getNumberOfLines();
    }
    public int getTotalProducts()throws IOException,TasteException
    {
        return model.getNumItems();
    }

    public int getTotalUsers()throws IOException,TasteException
    {
        return model.getNumUsers();
    }

    public List<String> getRecommendationsForUser(String user) throws IOException,TasteException {

        List<RecommendedItem> recommendations = cachingRecommender.recommend(memoryIDMigrator.toLongID(user),100);
        List<String> movieRecommenderList = new ArrayList<String>();
        for (int i=0; i < recommendations.size() ; i++) {
            movieRecommenderList.add(memoryIDMigrator.toStringID( recommendations.get(i).getItemID())); 
        }
        return movieRecommenderList;
    }



}//class