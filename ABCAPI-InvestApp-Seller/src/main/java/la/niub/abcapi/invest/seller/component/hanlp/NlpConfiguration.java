package la.niub.abcapi.invest.seller.component.hanlp;

import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
//@PropertySource("classpath:${spring.profiles.active}/nlp.properties")
@PropertySource("classpath:nlp.properties")
public class NlpConfiguration {

    @Value("${hanlp.root}/${nlp.chat.classify.corpus}")
    private String nlpChatClassifyCorpusPath;

    @Value("${hanlp.root}/${nlp.chat.classify.model}")
    private String nlpChatClassifyModelPath;

    @Value("${hanlp.root}/${nlp.dict.custom.company}")
    private String nlpDictCustomCompanyPath;

    @Value("${hanlp.root}/${nlp.dict.custom.concept}")
    private String nlpDictCustomConceptPath;

    @Value("${hanlp.root}/${nlp.dict.custom.analyst}")
    private String nlpDictCustomAnalystPath;

    @Value("${hanlp.root}/${nlp.dict.custom.places}")
    private String nlpDictCustomPlacesPath;

    @Value("${hanlp.root}/${nlp.dict.custom.city}")
    private String nlpDictCustomCityPath;

    @Value("${hanlp.root}/${nlp.dict.custom.person}")
    private String nlpDictCustomPersonPath;

    @Value("${hanlp.root}/${nlp.dict.custom.news.keywords}")
    private String nlpDictCustomNewsKeywordsPath;

    @Value("${hanlp.root}/${nlp.dict.custom.event.keywords}")
    private String nlpDictCustomEventKeywordsPath;

    @Value("${hanlp.root}/${nlp.dict.custom.broker}")
    private String nlpDictCustomBrokerPath;

    @Value("${hanlp.root}/${nlp.dict.custom.view.keywords}")
    private String nlpDictCustomViewKeywordsPath;

    @Value("${hanlp.root}/${nlp.dict.custom.company.synonyms}")
    private String nlpDictCustomCompanySynonymsPath;

    @Value("${hanlp.root}/${nlp.dict.custom.company.stock.code}")
    private String getNlpDictCustomCompanyStockCodePath;

    @Value("${hanlp.root}/${nlp.dict.custom.broker.synonyms}")
    private String getNlpDictCustomBrokerSynonymsPath;
    /**行业的词典路径*/
    @Value("${hanlp.root}/${nlp.dict.custom.industry}")
    private String nlpDictCustomIndustryPath;
    /**行业的近义词*/
    @Value("${hanlp.root}/${nlp.dict.custom.industry.synonyms}")
    private String industrySynonymsPath;

    @Value("${hanlp.root}/${nlp.dict.custom.nnt}")
    private String nlpDictNntPath;

    @Value("${hanlp.root}/${nlp.dict.custom.gi}")
    private String nlpDictGiPath;

    private Set<String> companyDict;
    private Set<String> conceptDict;
    private Set<String> newsKeywordsDict;
    private Set<String> analystDict;
    private Set<String> placesDict;
    private Set<String> cityDict;
    private Set<String> personDict;
    private Set<String> eventKeywordsDict;
    private Set<String> brokerDict;
    private Set<String> viewKeywordsDict;
    private Set<String> industryDict;
    private Map<String, String> companySynonyms;
    private Map<String, String> companyStockCode;
    private Map<String, String> brokerSynonyms;
    private Map<String, String> industrySynonymMap;


    @Bean
    public NlpConfiguration NlpConfiguration() throws IOException {
        //companyDict = loadWords(nlpDictCustomCompanyPath, "com 1024");
//        conceptDict = loadWords(nlpDictCustomConceptPath, "con 1024");
//        newsKeywordsDict = loadWords(nlpDictCustomNewsKeywordsPath, "new 1024");
//        analystDict = loadWords(nlpDictCustomAnalystPath, "ana 1024");
        cityDict = loadWords(nlpDictCustomCityPath, "cit 1024");
//        placesDict = loadWords(nlpDictCustomPlacesPath, "pla 1024");
//        personDict = loadWords(nlpDictCustomPersonPath, "nr 1024");
//        eventKeywordsDict = loadWords(nlpDictCustomEventKeywordsPath, "eve 1024");
        brokerDict = loadWords(nlpDictCustomBrokerPath, "bro 1024");
//        viewKeywordsDict = loadWords(nlpDictCustomViewKeywordsPath, "vie 1024");
        industryDict = loadWords(nlpDictCustomIndustryPath, "ind 1024");
//        companySynonyms = loadWords(nlpDictCustomCompanySynonymsPath);
        companyStockCode = loadWords(getNlpDictCustomCompanyStockCodePath);
        brokerSynonyms = loadWords(getNlpDictCustomBrokerSynonymsPath);
//        industrySynonymMap = loadWords(industrySynonymsPath);
        loadCustomDictionary();
        return new NlpConfiguration();
    }

    private Set<String> loadWords(String filePath, String natureWithFrequency) throws IOException {
        Set<String> words = new HashSet<>();
        Files.lines(Paths.get(filePath)).filter(line -> StringUtils.hasText(line)).forEach(words::add);
        Boolean ret = false;
        for (String word : words) {
            ret = CustomDictionary.add(word, natureWithFrequency);
            if(!ret)
            {
                continue;
            }
        }
        return words;
    }

    private Map<String, String> loadWords(String filePath) throws IOException {
        Map<String, String> resultMap = new HashMap<>(16);
        Set<String> words = new HashSet<>();
        Files.lines(Paths.get(filePath)).filter(line -> StringUtils.hasText(line)).forEach(words::add);
        for (String word : words) {
            String[] split = word.split("=");
            if (split.length < 2) {
                continue;
            }
            String value = split[0];
            String[] keys = split[1].split("/");
            for (int i = 0; i < keys.length; i++) {
                resultMap.put(keys[i], value);
            }
        }
        return resultMap;
    }

    public Set<String> getCompanyDict() {
        return companyDict;
    }

    public String getNlpChatClassifyCorpusPath() {
        return nlpChatClassifyCorpusPath;
    }

    public String getNlpChatClassifyModelPath() {
        return nlpChatClassifyModelPath;
    }

    public Set<String> getConceptDict() {
        return conceptDict;
    }

    public Set<String> getNewsKeywordsDict() {
        return newsKeywordsDict;
    }

    public Set<String> getAnalystDict() {
        return analystDict;
    }

    public Set<String> getPlacesDict() {
        return placesDict;
    }

    public Set<String> getCityDict() { return cityDict; }

    public Set<String> getPersonDict() {
        return personDict;
    }

    public Set<String> getEventKeywordsDict() {
        return eventKeywordsDict;
    }

    public Set<String> getBrokerDict() {
        return brokerDict;
    }

    public Set<String> getViewKeywordsDict() {
        return viewKeywordsDict;
    }

    public Map<String, String> getCompanySynonyms() {
        return companySynonyms;
    }

    public Map<String, String> getCompanyStockCode() {
        return companyStockCode;
    }

    public Map<String, String> getBrokerSynonyms() { return brokerSynonyms; }

    public Set<String> getIndustryDict() {
        return industryDict;
    }

    public Map<String, String> getIndustrySynonymMap() {
        return industrySynonymMap;
    }

    public Set<String> loadDictionary(String dicFile)
    {
        Set<String> dicwords = new HashSet<>();
        try
        {
            Set<String> words = new HashSet<>();
            Boolean ret = Boolean.FALSE;
            Files.lines(Paths.get(dicFile)).filter(line -> StringUtils.hasText(line)).forEach(words::add);
            for (String word : words) {
                String[] array = word.split("&");
                StringBuffer wordBuffer = new StringBuffer();
                if (array.length > 2) {
                    System.out.println(word);
                }
                for (int i = 0; i < array.length - 1; ++i) {
                    wordBuffer.append(array[i]);
                }
                ret = CustomDictionary.insert(wordBuffer.toString(), array[array.length - 1]);
                dicwords.add(wordBuffer.toString());
            }
        }catch (IOException e)
        {

        }
        return dicwords;
    }

    public void loadCustomDictionary()
    {
        loadDictionary(nlpDictNntPath);
//        companyDict = loadDictionary(nlpDictCustomCompanyPath);
        loadDictionary(nlpDictGiPath);
    }
}
