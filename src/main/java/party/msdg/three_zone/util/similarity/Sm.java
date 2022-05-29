package party.msdg.three_zone.util.similarity;

/**
 * Wow! Sweet moon.
 */
public class Sm {
    
    public static final  String content1="海湾新苑";
    
    public static final  String content2="海湾新苑：水电路1421弄1-11号、13-24号、26-33号";
    
    public static void main(String[] args) {
        
        double  score = CosineSimilarity.getSimilarity(content1,content2);
        System.out.println("相似度："+score);
        
        score=CosineSimilarity.getSimilarity(content1,content1);
        System.out.println("相似度："+score);
    }
}
