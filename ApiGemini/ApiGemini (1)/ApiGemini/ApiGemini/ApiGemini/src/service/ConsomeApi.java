package service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsomeApi {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyC5JsYUi70cS0-WPosSzOx5NmQ0-E77QEo";
    
    private static final Pattern RESPOSTA_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");
    
    public static String fazerPergunta(String pergunta) throws IOException, InterruptedException {
        String jsonRequest = gerarJsonRequest(pergunta);
        String respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson);
    }

    private static String extrairResposta(String respostaJson) {
        StringBuilder resposta = new StringBuilder();
        Iterator var2 = respostaJson.lines().toList().iterator();

        while(var2.hasNext()){
            String linha = (String)var2.next();
            Matcher matcher = RESPOSTA_PATTERN.matcher(linha);
            if (matcher.find()) {
                resposta.append(matcher.group(1)).append(" ");
            }
        }
            return "Resposta: " + resposta.toString().trim();
    }

    public static String formatarResposta(String resposta) {
        resposta = resposta.replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"");
        StringBuilder respostaFormatada = new StringBuilder();
        int maxLength = 80;
        int i = 0;

        while (i < resposta.length()) {
            int fimLinha = Math.min(i + maxLength, resposta.length());
            if (fimLinha < resposta.length() && resposta.charAt(fimLinha) != ' ') {
                int ultimoEspaco = resposta.lastIndexOf(' ', fimLinha);
                if (ultimoEspaco > i) {
                    fimLinha = ultimoEspaco;
                }
            }
            respostaFormatada.append(resposta, i, fimLinha).append("\n");
            i = fimLinha + 1; // Pula o espaço
        }
        return "Resposta formatada:\n" + respostaFormatada.toString().trim();
    }

    private static String enviarRequisicao(String jsonRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        
    }

    private static String gerarJsonRequest(String pergunta) {
        String promptFormatado = "O resultado gerado não deve possuir formatação ou caracteres especiais. Pergunta: " + pergunta;
        return "{\"contents\":[{\"parts\":[{\"text\":\""+ promptFormatado + "\"}]}]}";
    }
}
