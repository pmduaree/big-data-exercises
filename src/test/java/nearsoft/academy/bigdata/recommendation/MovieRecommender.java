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
    public MovieRecommender(String path)throws IOException,TasteException{    //constructor de la clase
        //TextReducer.textReducer(path); //create a file names temp.csv with the parsed file

        memoryIDMigrator = new MemoryIDMigrator();
        textReducer = new TextReducer(path, memoryIDMigrator);
        textReducer.clean();

        model = new FileDataModel(new File("temp.csv"));
        similarity = new PearsonCorrelationSimilarity(model);
        neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        cachingRecommender = new CachingRecommender(recommender);


    }
    public int getTotalReviews()
    {
        return 7911684;
    }
    public int getTotalProducts()throws IOException,TasteException
    {
        return model.getNumItems();
        //System.out.println(total);  //print the total of products
    }

    public int getTotalUsers()throws IOException,TasteException
    {
        return model.getNumUsers();
        //System.out.println(total);  //print the total of users
    }

    public List<String> getRecommendationsForUser(String user) throws IOException,TasteException {
        //int User = Integer.parseInt(user);

        //List<RecommendedItem> recommendations = cachingRecommender.recommend(,100);
        //System.out.println(memoryIDMigrator.toLongID(user));

        List<RecommendedItem> recommendations = cachingRecommender.recommend(memoryIDMigrator.toLongID(user),100);
        List<String> movieRecommenderList = new ArrayList<String>();
        //System.out.println(recommendations.size());
        for (int i=0; i < recommendations.size() ; i++) {
            movieRecommenderList.add(memoryIDMigrator.toStringID( recommendations.get(i).getItemID())); //here we need to remap to the other way using memoryIdMigrator map subclass
        }
        //System.out.println(recommendations);  //print all recommendations
        System.out.println(movieRecommenderList);
        return movieRecommenderList;
    }



}//class