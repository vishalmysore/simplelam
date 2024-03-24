<div align="center">
  <a href="https://www.linkedin.com/posts/vishalrow_ai-appdevelopment-actions-activity-7171302152101900288-64qg?utm_source=share&utm_medium=member_desktop">
    <img src="tools4ai.png"  width="300" height="300">
  </a>
</div>
<p align="center">
    <img  src="https://api.visitorbadge.io/api/visitors?path=https%3A%2F%2Fgithub.com%2Fvishalmysore%2Ftools4ai&countColor=black&style=flat%22">
    <a target="_blank" href="https://github.com/vishalmyore/tools4ai"><img src="https://img.shields.io/github/stars/vishalmysore/tools4ai?color=black" /></a>    
</p>

# 🎬 Simple Action Model - SAM

This is reference implementation of Tool4AI project  https://github.com/vishalmysore/Tools4AI
Basically showcasing how straight forward it is to build action oriented applications in 100% Java 

## Setup
Clone this project and then  

```mvn clean install```

## Quick Start
Inside ```Main.java``` these 2 lines will predict the action and execute it using Gemini 

```
String cookPromptSingleText = "My friends name is Vishal ," +
                "I dont know what to cook for him today.";
ActionProcessor processor = new ActionProcessor();
String result = (String)processor.processSingleAction(cookPromptSingleText);
log.info(result);
```

This code will use OpenAI to predict the action and execute it 

```
OpenAiActionProcessor opeAIprocessor = new OpenAiActionProcessor();
Sring result = (String)opeAIprocessor.processSingleAction('My friends name is Vishal ,he lives in tornto.I want save this info locally');
System.out.println(result);

```

Create custom action by implementing ```JavaMethodAction ``` interface  

```
@Predict(actionName = "whatFoodDoesThisPersonLike", description = "what is the food preference of this person ")
public class SimpleAction implements JavaMethodAction {

    public String whatFoodDoesThisPersonLike(String name) {
        if("vishal".equalsIgnoreCase(name))
            return "Paneer Butter Masala";
        else if ("vinod".equalsIgnoreCase(name)) {
            return "aloo kofta";
        }else
            return "something yummy";
    }

}
```
or
```
@Log
@Predict(actionName = "googleSearch", description = "search the web for information")
public class SearchAction implements JavaMethodAction {


    public String googleSearch(String searchString, boolean isNews)  {
        log.info(searchString+" : "+isNews);
        HttpResponse<String> response = Unirest.post("https://google.serper.dev/search")
                .header("X-API-KEY", PredictionLoader.getInstance().getSerperKey())
                .header("Content-Type", "application/json")
                .body("{\"q\":\""+searchString+"\"}")
                .asString();
        String resStr = response.getBody().toString();
        return resStr;
    }




}
```

Or add actions in Shell or HTTP config files  


You can add Human In Loop validation , Explainablity , Multi Command Processor, Hallucination Detector , Bias Detector , Database and Tibco actions as well
please look at https://github.com/vishalmysore/Tools4AI for more information

## Prompt Transformer

Prompt Transformer, a core feature in the Tools4AI project, simplifies data transformation tasks. It effortlessly converts prompts into various formats like Java POJOs, JSON strings, CSV files, and XML. By enabling direct conversion of prompts into domain-specific objects, Prompt Transformer streamlines data processing tasks. It offers flexibility and ease of use for transforming data structures to meet diverse needs in modern applications.

### Convert to Simple Pojo

Lets take the first scenario where you want to conver the prompt directly into Java Bean or Pojo

```  
PromptTransformer builder = new PromptTransformer();
String promptTxt ="Sachin Tendulkar is very good cricket player, " +
                           "he joined the sports on 24032022, he has played 300 matches " +
                           "and his max score is 400";
//Convert the prompt to Pojo
Player player = (Player)builder.transformIntoPojo(promptTxt, Player.class.getName(),"Player","create player pojo");
log.info(player.toString());
```

The above will convert the prompt into this simple Pojo 
```
import lombok.*;
import lombok.extern.java.Log;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Player {
     int matches;
     int maxScore;
     String firstName;
     String lastName;
     Date dateJoined;


}
 
```
### Convert to Complex Pojo

The transformer can also convert into complex Pojo ( where there are multiple objects inside the Pojo)

```
PromptTransformer builder = new PromptTransformer();
promptTxt = "can you book Maharaja restaurant in " +
            "Toronto for 4 people on 12th may , I am Vishal ";
//Convert the prompt to Complex Pojo
RestaurantPojo pojo = (RestaurantPojo)builder.transformIntoPojo(promptTxt, RestaurantPojo.class.getName(),"RestaurantPojo","Build the pojo for restaurant");
log.info(pojo.toString()); 
```

This will create the Pojo Object of RestaurantPojo and also populate the internal objects ( not just primitive)
```
public class RestaurantPojo {
    String name;
    int numberOfPeople;
    //Pojo inside Pojo    
    RestaurantDetails restaurantDetails;
    boolean cancel;
    String reserveDate; 
```
### Convert with Custom GSON

If you expect some custom objects like Date etc in the prompt you can have custom Gson Builder

```
//Using Custom GSON to convert special values
GsonBuilder gsonBuilder = new GsonBuilder();
gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer("dd MMMM yyyy"));
Gson gson = gsonBuilder.create();
PromptTransformer customBuilder = new PromptTransformer(gson);
String prompt = "Sachin Tendulkar is very good cricket player, he joined the sports on 12 May 2008," +
                "he has played 300 matches and his max score is 400";
player = (Player)customBuilder.transformIntoPojo(prompt, Player.class.getName(),"Player","create player pojo");
log.info(player.toString()); 
```
This will use Custom Date Serializer 

```
public class DateDeserializer implements JsonDeserializer<Date> {
    private final DateFormat dateFormat;

    public DateDeserializer(String format) {
        this.dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return dateFormat.parse(json.getAsString().replaceAll("(st|nd|rd|th),", ","));
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
} 
```

### Convert to Json String


## Java Doc
https://javadoc.io/doc/io.github.vishalmysore/tools4ai/latest/com/t4a/api/AIAction.html

## MVN Dependency

https://repo1.maven.org/maven2/io/github/vishalmysore/tools4ai/