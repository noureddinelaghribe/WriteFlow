//package com.noureddine.phraseflow.repositorys;
//
//import com.noureddine.phraseflow.interfaces.DeepSeekApiService;
//import com.noureddine.phraseflow.model.DeepSeekRequest;
//import com.noureddine.phraseflow.model.DeepSeekResponse;
//import com.noureddine.phraseflow.model.Message;
//import com.noureddine.phraseflow.model.TypeProcessing;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//
//public class DeepSeekRepository {
//    private final DeepSeekApiService apiService;
//
//    public DeepSeekRepository() {
//        this.apiService = RetrofitClient.getApiService();
//    }
//
//    public Call<DeepSeekResponse> getChatCompletion(TypeProcessing typeProcessing , String apiKey) {
//        List<Message> messages = new ArrayList<>();
//
//        switch (typeProcessing.getType()){
//            case "AI Detector":
//                messages.add(new Message("system", "Act as an advanced AI detector. Analyze the provided text for AI-generated content by evaluating: Structural Patterns : Repetitive phrases, formulaic transitions (e.g., 'current trends indicate'), or unnatural lists 2. Technical Jargon : Overuse of terms like 'low-perplexity phrases' or 'multi-modal algorithms' 46. Coherence : Lack of nuanced reasoning or personal anecdotes 9. Hallucinations : Claims of functionality unsupported by context (e.g., 'sustainable materials' without real-world examples) 7. Timestamps/Context : Anachronisms (e.g., 'February 2025' on a 2023 dataset) 1. Output : Return only the detected AI probability as an integer (0-100) . Apply all filters to minimize false positives. Examples : Sample Text : 'The sky is blue.' → Output: '0' Sample Text : 'AI algorithms optimize engine mapping for peak efficiency' → Output: '40'. Do not accept any other type of request."));
//                break;
//            case "Grammar Checker":
//                messages.add(new Message("system", "Act as an advanced Grammar Checker. I want you to respond only in "+typeProcessing.getLanguage()+". Your task is to Check the grammar and punctuation and Grammar Error Percentage: [INTEGER]% Please Do not echo my prompt. Do not remind me what I asked you for. Do not apologize. Do not self-reference. Just take the best action you can. All output must be in the "+typeProcessing.getLanguage()+". Do not accept any other type of request."));
//                break;
//            case "Paragraph Generator":
//                String keywords = "with this keywords "+typeProcessing.getKeywords();
//                messages.add(new Message("system",
//                        "Respond exclusively in " + typeProcessing.getLanguage() + ". Assume the role of an expert elite copywriter fluent in " + typeProcessing.getLanguage() + ". Your task is to write a 1500 to 2500 word article "+ (typeProcessing.getKeywords().isEmpty() ? "" : keywords) +". Ensure the article is 100% unique, plagiarism-free, and written in a natural, human style with impeccable grammar (similar to grammarly.com). Be precise and direct, using active voice and incorporating at least 30% transition words throughout. All sentences must be less than 20 words and avoid consecutive sentence structures. Do not explain your process—simply deliver the best possible article. All output must be in " + typeProcessing.getLanguage() + " and no other type of request will be accepted."
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
//
//        messages.add(new Message("user", typeProcessing.getText()));
//        DeepSeekRequest request = new DeepSeekRequest(messages,0.7,5000);
//        return apiService.getChatCompletion("Bearer " + apiKey, request);
//    }
//}
