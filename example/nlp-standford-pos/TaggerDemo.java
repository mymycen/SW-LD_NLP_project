import edu.stanford.nlp.util.logging.Redwood;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TaggerDemo  {

  /** A logger for this class */
  private static Redwood.RedwoodChannels log = Redwood.channels(TaggerDemo.class);

  private TaggerDemo() {}

  public static void main(String[] args) throws Exception {
    MaxentTagger tagger = new MaxentTagger("C:\\Users\\lenovo\\Downloads\\stanford-postagger-full-2017-06-09\\stanford-postagger-full-2017-06-09\\models\\english-caseless-left3words-distsim.tagger");
    String sample = "Where I can find a KFC in Berlin";
    String tagged = tagger.tagString(sample);
    System.out.println(tagged);
  }

}
