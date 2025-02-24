package richard.test.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by richard on 17/04/2019.
 */
public class InvertedIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvertedIndex.class);

    /**
     * key : word
     * value : filePath list.
     */
    private Map<String, ArrayList<String>> iIdx = new HashMap<>();

    /**
     * file path list.
     */
    private ArrayList<String> list;

    /**
     * word freq.
     * key: work
     * value: frequency
     */
    private Map<String, Integer> wordFreq = new HashMap<>();

    public void createIndex(String filePath) {

//        String[] words = null;
        Set<String> words = new HashSet<>();
        try {

            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = reader.readLine()) != null) {
                //获取单词
                words.addAll(Arrays.asList(s.split(" ")));

            }
            words.forEach(word -> {
                if (!iIdx.containsKey(word)) {
                    list = new ArrayList<>();
                    list.add(filePath);
                    iIdx.put(word, list);
                    wordFreq.put(word, 1);
                } else {
                    list = iIdx.get(word);
                    //如果没有包含过此文件名，则把文件名放入
                    if (!list.contains(filePath)) {
                        list.add(filePath);
                    }
                    //文件总词频数目
                    int count = wordFreq.get(word) + 1;
                    wordFreq.put(word, count);
                }
            });
            reader.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        for (int i = 1; i <= 3; i++) {
            String path="/Users/richard/tmp/" + i + ".txt";
            index.createIndex(path);
        }
        for (Map.Entry<String, ArrayList<String>> map : index.iIdx.entrySet()) {
            LOGGER.info("{}: {}", map.getKey(), map.getValue());
        }

        for (Map.Entry<String, Integer> num : index.wordFreq.entrySet()) {
            LOGGER.info("{}: {}", num.getKey(), +num.getValue());
        }
    }

}
