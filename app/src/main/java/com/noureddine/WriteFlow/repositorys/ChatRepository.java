package com.noureddine.WriteFlow.repositorys;

import android.util.Log;

import com.noureddine.WriteFlow.interfaces.OpenAIService;
import com.noureddine.WriteFlow.model.ChatRequest;
import com.noureddine.WriteFlow.model.ChatResponse;
import com.noureddine.WriteFlow.model.Message;
import com.noureddine.WriteFlow.model.TypeProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ChatRepository.java
public class ChatRepository {
    private static final String BASE_URL = "https://api.openai.com/v1/";
    private static final String API_KEY = ""; // Store securely
    private final OpenAIService openAIService;

    public ChatRepository() {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor.Level.BODY))
//                .build();

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

        openAIService = retrofit.create(OpenAIService.class);
    }

    public void sendTodo(TypeProcessing typeProcessing , final ChatCallback callback) {
        List<Message> messages = new ArrayList<>();
        // هنا يمكنك تعديل الكود لتقييد الاستخدام بحيث يكون فقط لإعادة صياغة المقالات
        // على سبيل المثال: يمكنك إضافة رسالة نظام تُحدد بأن الغرض هو إعادة الصياغة فقط.
        //messages.add(new Message("system", "Your only task is to rewrite the provided article to make it more persuasive."));

//        switch (typeProcessing.getType()){
//            case "AI Detector":
//                messages.add(new Message("system", "Act as an advanced AI detector. Analyze the provided text for AI-generated content by evaluating: Structural Patterns : Repetitive phrases, formulaic transitions (e.g., 'current trends indicate'), or unnatural lists 2. Technical Jargon : Overuse of terms like 'low-perplexity phrases' or 'multi-modal algorithms' 46. Coherence : Lack of nuanced reasoning or personal anecdotes 9. Hallucinations : Claims of functionality unsupported by context (e.g., 'sustainable materials' without real-world examples) 7. Timestamps/Context : Anachronisms (e.g., 'February 2025' on a 2023 dataset) 1. Output : Return only the detected AI probability as an integer (0-100) . Apply all filters to minimize false positives. Examples : Sample Text : 'The sky is blue.' → Output: '0' Sample Text : 'AI algorithms optimize engine mapping for peak efficiency' → Output: '40'. Do not accept any other type of request."));
//                break;
//            case "Grammar Checker":
//                messages.add(new Message("system", "Act as an advanced Grammar Checker. I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to Check the grammar and punctuation and Grammar Error Percentage: [INTEGER]% Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+". Do not accept any other type of request."));
//                break;
//            case "Paragraph Generator":
//                messages.add(new Message("system",
//                        "Respond exclusively in " + typeProcessing.getLanguage() + ". " +
//                                "Assume the role of an expert SEO specialist and elite copywriter fluent in " + typeProcessing.getLanguage() + ". " +
//                                "Your task is to write a 1500 to 2500 word article starting with a bold SEO title and including subheadings with related keywords. " +
//                                "Ensure the article is 100% unique, plagiarism-free, and written in a natural, human style with impeccable grammar (similar to grammarly.com). " +
//                                "Be precise and direct, using active voice and incorporating at least 30% transition words throughout. " +
//                                "All sentences must be less than 20 words and avoid consecutive sentence structures. " +
//                                "Do not explain your process—simply deliver the best possible article. " +
//                                "All output must be in " + typeProcessing.getLanguage() + " and no other type of request will be accepted."
//                ));
//                break;
//            case "Paraphraser / Rewriting":
//                messages.add(new Message("system", "I want you to respond only in "+typeProcessing.getLanguage()+". I want you to act as a highly skilled blogger and top-tier copywriter who is fluent in "+typeProcessing.getLanguage()+". I want you to pretend that you are an expert at writing and managing paragraphs in "+typeProcessing.getLanguage()+". Your task is to Rewrite the paragraph that I will provide at the end to make it more persuasive. Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+" After answering, Please write in "+typeProcessing.getMode()+" writing style. Your only task is to rewrite any paragraph provided to make it more persuasive while maintaining clarity and engagement. Do not accept any other type of request."));
//                break;
//            case "Summarizer":
//                messages.add(new Message("system", "Act as an advanced summarizer I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to summarize the given article. The article must be 300 to 2500 words. Article must be 100% human writing style, fix grammar issues and change to active voice. Do not accept any other type of request."));
//                break;
//
//        }


        switch (typeProcessing.getType()){
            case "AI Detector":
                messages.add(new Message("system", "Act as an advanced AI detector. Analyze the provided text for AI-generated content by evaluating: Structural Patterns : Repetitive phrases, formulaic transitions (e.g., 'current trends indicate'), or unnatural lists 2. Technical Jargon : Overuse of terms like 'low-perplexity phrases' or 'multi-modal algorithms' 46. Coherence : Lack of nuanced reasoning or personal anecdotes 9. Hallucinations : Claims of functionality unsupported by context (e.g., 'sustainable materials' without real-world examples) 7. Timestamps/Context : Anachronisms (e.g., 'February 2025' on a 2023 dataset) 1. Output : Return only the detected AI probability as an integer (0-100) . Apply all filters to minimize false positives. Examples : Sample Text : 'The sky is blue.' → Output: '0' Sample Text : 'AI algorithms optimize engine mapping for peak efficiency' → Output: '40'. Do not accept any other type of request."));
                break;
            case "Grammar Checker":
                //messages.add(new Message("system", "Act as an advanced Grammar Checker. I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to Check the grammar and punctuation and Grammar Error Percentage: [INTEGER]% Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+". Do not accept any other type of request."));

                messages.add(new Message("system", "Act as an advanced Grammar Checker. I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to Check the grammar and punctuation and Grammar Error Percentage: [INTEGER]% Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+". Do not accept any other type of request. \n\n" +
                        "### Output Format:" +
                        "{"+
                        "\"text\": \"[Corrected text here]\","+
                        "\"issue\": [INTEGER percentage],"+
                        "}"
                        ));

                break;
            case "Paragraph Generator":
//                String keywords = "with this keywords "+typeProcessing.getKeywords();
//                messages.add(new Message("system",
//                        "Respond exclusively in " + typeProcessing.getLanguage() + ". Assume the role of an expert elite copywriter fluent in " + typeProcessing.getLanguage() + ". Your task is to write a 1500 to 2500 word article "+ (typeProcessing.getKeywords().isEmpty() ? "" : keywords) +". Ensure the article is 100% unique, plagiarism-free, and written in a natural, human style with impeccable grammar (similar to grammarly.com). Be precise and direct, using active voice and incorporating at least 30% transition words throughout. All sentences must be less than 20 words and avoid consecutive sentence structures. Do not explain your process—simply deliver the best possible article. All output must be in " + typeProcessing.getLanguage() + " and no other type of request will be accepted."
//                ));

                String keywords = "with keywords: " + typeProcessing.getKeywords();
                messages.add(new Message("system",
                        "### Instructions\n"+
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
                                "DO NOT EXPLAIN PROCESS OR ADD ANY EXTRAS."));

                break;
            case "Paraphraser / Rewriting":
//                messages.add(new Message("system", "I want you to respond only in "+typeProcessing.getLanguage()+". I want you to act as a highly skilled blogger and top-tier copywriter who is fluent in "+typeProcessing.getLanguage()+". I want you to pretend that you are an expert at writing and managing paragraphs in "+typeProcessing.getLanguage()+". Your task is to Rewrite the paragraph that I will provide at the end to make it more persuasive. Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+" After answering, Please write in "+typeProcessing.getMode()+" writing style. Your only task is to rewrite any paragraph provided to make it more persuasive while maintaining clarity and engagement. Do not accept any other type of request."));

                messages.add(new Message("system",
                        "### Instructions\n"+
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
                                "DO NOT EXPLAIN PROCESS OR ADD UNREQUESTED CONTENT."));

                break;
            case "Summarizer":
//                messages.add(new Message("system", "Act as an advanced summarizer I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to summarize the given article. The article must be 300 to 2500 words. Article must be 100% human writing style, fix grammar issues and change to active voice. Do not accept any other type of request."));

                messages.add(new Message("system",
                        "### Instructions\n"+
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
                        "DO NOT EXPLAIN PROCESS OR ADD UNREQUESTED CONTENT."));

                break;

        }


        messages.add(new Message("user", typeProcessing.getText()));

        ChatRequest request = new ChatRequest(messages);

        openAIService.createChatCompletion("Bearer " + API_KEY, request)
                .enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful() && response.body() != null &&
                                response.body().getChoices() != null &&
                                !response.body().getChoices().isEmpty()) {

                            String responseMessage = response.body().getChoices().get(0).getMessage().getContent();
                            Log.d("TAG", "onResponse: "+response.body().getUsage().getTotal_tokens());

                            callback.onSuccess(responseMessage);
                        } else {
                            callback.onError("Error: " + (response.errorBody() != null ?
                                    response.errorBody().toString() : "Unknown error"));
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        callback.onError("Network Error: " + t.getMessage());
                    }
                });
    }



    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}
