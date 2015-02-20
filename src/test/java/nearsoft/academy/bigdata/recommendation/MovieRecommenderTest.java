package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.Test;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class MovieRecommenderTest {
    @Test
    public void testDataInfo() throws IOException, TasteException {
        //download movies.txt.gz from 
        //    http://snap.stanford.edu/data/web-Movies.html
        MovieRecommender recommender = new MovieRecommender("/home/pduarte/Nearsoft/big-data/movies.txt.gz");
        assertEquals(7911684, recommender.getTotalReviews());
        assertEquals(253059, recommender.getTotalProducts());
        assertEquals(889176, recommender.getTotalUsers());

        List<String> recommendations = recommender.getRecommendationsForUser("A141HP4LYPWMSR");
        assertThat(recommendations, hasItem("B00005NGKN"));
        assertThat(recommendations, hasItem("B00388PK1A"));
        assertThat(recommendations, hasItem("B001HZ4K7Q"));

    }

}
