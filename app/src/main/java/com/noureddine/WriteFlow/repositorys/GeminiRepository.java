package com.noureddine.WriteFlow.repositorys;


import com.noureddine.WriteFlow.interfaces.GeminiApiService;
import com.noureddine.WriteFlow.model.Content;
import com.noureddine.WriteFlow.model.GeminiRequest;
import com.noureddine.WriteFlow.model.GeminiResponse;
import com.noureddine.WriteFlow.model.Part;
import com.noureddine.WriteFlow.model.SystemInstruction;
import com.noureddine.WriteFlow.model.TypeProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeminiRepository {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    private final GeminiApiService apiService;
    private final String apiKey;

    public GeminiRepository(String apiKey) {
        this.apiKey = apiKey;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(GeminiApiService.class);
    }


    SystemInstruction sysInstruction;
    List<Part> systemParts;

    public void sendTodo(TypeProcessing typeProcessing , final Callback<GeminiResponse> callback) {



        switch (typeProcessing.getType()){
            case "AI Detector":

                Part systemPartAD = new Part("Act as an advanced AI detector. Analyze the provided text for AI-generated content by evaluating: Structural Patterns : Repetitive phrases, formulaic transitions (e.g., 'current trends indicate'), or unnatural lists 2. Technical Jargon : Overuse of terms like 'low-perplexity phrases' or 'multi-modal algorithms' 46. Coherence : Lack of nuanced reasoning or personal anecdotes 9. Hallucinations : Claims of functionality unsupported by context (e.g., 'sustainable materials' without real-world examples) 7. Timestamps/Context : Anachronisms (e.g., 'February 2025' on a 2023 dataset) 1. Output : Return only the detected AI probability as an integer (0-100) . Apply all filters to minimize false positives. Examples : Sample Text : 'The sky is blue.' → Output: '0' Sample Text : 'AI algorithms optimize engine mapping for peak efficiency' → Output: '40'. Do not accept any other type of request.");
                systemParts = new ArrayList<>();
                systemParts.add(systemPartAD);
                sysInstruction = new SystemInstruction(systemParts);

                break;
            case "Grammar Checker":

                Part systemPartGC = new Part("Act as an advanced Grammar Checker. I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to Check the grammar and punctuation and Grammar Error Percentage: [INTEGER]% Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+". Do not accept any other type of request." +
                        "### Output Format :" +
                        "{"+
                        "\"text\": \"[Corrected text here]\","+
                        "\"issue\": [INTEGER percentage],"+
                        "}");
                systemParts = new ArrayList<>();
                systemParts.add(systemPartGC);
                sysInstruction = new SystemInstruction(systemParts);
                break;
            case "Paragraph Generator":

                String keywords = "with keywords: " + typeProcessing.getKeywords();
                Part systemPartPG = new Part("### Instructions\n"+
                                "1. Write a **1500-2500 word article** in " + typeProcessing.getLanguage() + ".\n"+
                                "2. Incorporate these keywords naturally: " + keywords + ".\n"+
                                "3. Please write in "+typeProcessing.getMode()+
                                "4. Ensure **100% uniqueness** and **plagiarism-free** content.\n"+
                                "5. Use **active voice** exclusively, with **30% transition words** (e.g., however, furthermore, consequently).\n"+
                                "6. Every sentence must be:\n"+
                                "   - <20 words\n"+
                                "   - No consecutive similar structures\n"+
                                "7. Grammar quality must match Grammarly’s standards.\n"+
                                "8. Output only the article content in " + typeProcessing.getLanguage() + ".\n"+

                                "Style Guidelines:\n" +
                                "- Use active voice (minimum 85%)\n" +
                                "- Mix sentence structures (12-20 words)\n" +
                                "- Apply natural transitions between paragraphs\n" +
                                "- Avoid all special symbols (*, •, → etc)\n\n" +

                                "Strict Prohibitions:\n" +
                                "- No self-references\n" +
                                "- No markdown formatting\n" +
                                "- No filler content"+

                                "  Example format:\n"+
                                "[مقالتك هنا]\n"+
                                "DO NOT EXPLAIN PROCESS OR ADD ANY EXTRAS.");
                systemParts = new ArrayList<>();
                systemParts.add(systemPartPG);
                sysInstruction = new SystemInstruction(systemParts);
                break;
            case "Paraphraser / Rewriting":

                Part systemPartPR = new Part("### Instructions\n"+
                                "1. **Respond exclusively in " + typeProcessing.getLanguage() + "**. \n"+
                                "2. **Act as a top-tier copywriter and persuasive blogger** with expertise in " + typeProcessing.getLanguage() + " content creation.\n"+
                                "3. **Rewrite the provided paragraph** to enhance persuasiveness while maintaining clarity and engagement. \n"+
                                "4. **Output requirements**:\n"+
                                "   - No extra text (e.g., explanations, apologies, or self-references).\n"+
                                "   - Follow the **" + typeProcessing.getMode() + "** writing style (e.g., formal, creative, technical).\n"+
                                "5. **Focus solely on rewriting**—do not accept other requests.\n"+

                                "Style Guidelines:\n" +
                                "- Use active voice (minimum 85%)\n" +
                                "- Mix sentence structures (12-20 words)\n" +
                                "- Apply natural transitions between paragraphs\n" +
                                "- Avoid all special symbols (*, •, → etc)\n\n" +

                                "Strict Prohibitions:\n" +
                                "- No self-references\n" +
                                "- No markdown formatting\n" +
                                "- No filler content"+

                                "### Example format:\n"+
                                "[النص المُعدّل هنا]\n"+
                                "DO NOT EXPLAIN PROCESS OR ADD UNREQUESTED CONTENT.");
                systemParts = new ArrayList<>();
                systemParts.add(systemPartPR);
                sysInstruction = new SystemInstruction(systemParts);
                break;
            case "Summarizer":

                Part systemPartS = new Part("### Instructions\n"+
                                "1. **Act as an advanced summarizer** fluent in " + typeProcessing.getLanguage() + ".\n"+
                                "2. **Summarize the provided article** (10%-30% of the total words) into a concise, **human-written style** with the following requirements:\n"+
                                "   - **Grammar**: Fix all errors to meet Grammarly standards <button class=citation-flag data-index=5><button class=citation-flag data-index=6>.\n"+
                                "   - **Voice**: Convert all sentences to **active voice**.\n"+
                                "   - **Clarity**: Eliminate redundancy while retaining core ideas <button class=citation-flag data-index=1><button class=citation-flag data-index=2>.\n"+
                                "   - **Length**: Keep sentences <20 words to ensure readability <button class=citation-flag data-index=6><button class=citation-flag data-index=8>.\n"+
                                "3. **Output format**:\n"+
                                "   - <button class=citation-flag data-index=5><button class=citation-flag data-index=8>.\n"+
                                "   - No explanations, apologies, or extra text.\n"+
                                "4. **Focus solely on summarization**: Do not accept other requests.\n"+

                                "Style Guidelines:\n" +
                                "- Use active voice (minimum 85%)\n" +
                                "- Mix sentence structures (12-20 words)\n" +
                                "- Apply natural transitions between paragraphs\n" +
                                "- Avoid all special symbols (*, •, → etc)\n\n" +

                                "Strict Prohibitions:\n" +
                                "- No self-references\n" +
                                "- No markdown formatting\n" +
                                "- No filler content"+

                                "### Example format:\n"+
                                "[ملخص النص هنا باللغة المطلوبة]\n"+
                                "DO NOT EXPLAIN PROCESS OR ADD UNREQUESTED CONTENT.");
                systemParts = new ArrayList<>();
                systemParts.add(systemPartS);
                sysInstruction = new SystemInstruction(systemParts);
                break;

        }


        // Create parts for user content
        Part userPart = new Part(typeProcessing.getText());
        List<Part> userParts = new ArrayList<>();
        userParts.add(userPart);

        // Create content
        Content content = new Content(userParts, "user");
        List<Content> contents = new ArrayList<>();
        contents.add(content);

        // Create full request
        GeminiRequest request = new GeminiRequest(sysInstruction, contents);

        apiService.generateContent(apiKey, request).enqueue(callback);


    }














}