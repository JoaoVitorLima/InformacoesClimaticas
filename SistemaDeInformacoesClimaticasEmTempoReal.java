import org.json.JSONObject;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class SistemaDeInformacoesClimaticasEmTempoReal {
    
    // Constantes para valores fixos (Melhoria 7)
    private static final String ARQUIVO_API_KEY = "api-key.txt";
    private static final String MENSAGEM_ERRO_LOCALIZACAO = "Localização não encontrada. Por favor, tente novamente.";
    private static final String MENSAGEM_ERRO_GERAL = "Erro ao processar a solicitação: ";
    private static final String SOLICITACAO_CIDADE = "Digite o nome da cidade: ";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.print(SOLICITACAO_CIDADE);
            String cidade = scanner.nextLine().trim();
            
            // Validação de entrada (Melhoria 6)
            if (cidade.isEmpty()) {
                System.out.println("Erro: O nome da cidade não pode estar vazio.");
                return;
            }
            
            if (!contemApenasLetrasEEspacos(cidade)) {
                System.out.println("Erro: O nome da cidade deve conter apenas letras e espaços.");
                return;
            }
            
            String dadosClimaticos = getDadosClimaticos(cidade);

            // Código 1006 significa localização não encontrada.
            if (dadosClimaticos.contains("\"code\":1006")) {
                System.out.println(MENSAGEM_ERRO_LOCALIZACAO);
            } else {
                imprimirDadosClimaticos(dadosClimaticos);
            }
        } catch (Exception e) {
            // Tratamento de erros robusto (Melhoria 3)
            System.out.println(MENSAGEM_ERRO_GERAL + e.getMessage());
            System.out.println("Verifique sua conexão com a internet e tente novamente.");
        } finally {
            scanner.close();
        }
    }

    public static String getDadosClimaticos(String cidade) throws Exception {
        // Verifica se o arquivo de API key existe (Melhoria 3)
        if (!Files.exists(Paths.get(ARQUIVO_API_KEY))) {
            throw new Exception("Arquivo api-key.txt não encontrado. Por favor, crie um arquivo com sua chave API.");
        }
        
        String apiKey = Files.readString(Paths.get(ARQUIVO_API_KEY)).trim();
        
        // Validação da chave API (Melhoria 6)
        if (apiKey.isEmpty()) {
            throw new Exception("Chave API não encontrada no arquivo api-key.txt.");
        }
        
        String formataNomeCidade = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + formataNomeCidade;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET() // Método explícito para melhor clareza (Melhoria 7)
            .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Verificação do status HTTP (Melhoria 3)
            if (response.statusCode() != 200) {
                throw new Exception("Erro na API: Código " + response.statusCode() + " - " + response.body());
            }
            
            return response.body();
        } catch (java.net.ConnectException e) {
            throw new Exception("Não foi possível conectar ao serviço meteorológico. Verifique sua conexão com a internet.");
        } catch (java.net.UnknownHostException e) {
            throw new Exception("Não foi possível resolver o host da API. Verifique sua conexão com a internet.");
        } catch (Exception e) {
            throw new Exception("Erro de conexão: " + e.getMessage());
        }
    }

    public static void imprimirDadosClimaticos(String dados) {
        try {
            JSONObject dadosJson = new JSONObject(dados);
            JSONObject informacoesMeteorologicas = dadosJson.getJSONObject("current");

            // Extrai os dados da localização
            String cidade = dadosJson.getJSONObject("location").getString("name");
            String pais = dadosJson.getJSONObject("location").getString("country");

            // Extrai os dados adicionais
            String condicaoTempo = informacoesMeteorologicas.getJSONObject("condition").getString("text");
            int umidade = informacoesMeteorologicas.getInt("humidity");
            float velocidadeVento = informacoesMeteorologicas.getFloat("wind_kph");
            float pressaoAtmosferica = informacoesMeteorologicas.getFloat("pressure_mb");
            float sensacaoTermica = informacoesMeteorologicas.getFloat("feelslike_c");
            float temperaturaAtual = informacoesMeteorologicas.getFloat("temp_c");

            // Extrai a data e hora da String retornada pela API
            String dataHoraString = informacoesMeteorologicas.getString("last_updated");

            // Imprimir informações atuais
            System.out.println("\n==========================================");
            System.out.println("INFORMAÇÕES METEOROLÓGICAS - " + cidade.toUpperCase());
            System.out.println("==========================================");
            System.out.println("Localização: " + cidade + ", " + pais);
            System.out.println("Data e Hora: " + dataHoraString);
            System.out.println("------------------------------------------");
            System.out.println("Temperatura Atual: " + temperaturaAtual + "°C");
            System.out.println("Sensação Térmica: " + sensacaoTermica + "°C");
            System.out.println("Condição do Tempo: " + condicaoTempo);
            System.out.println("Umidade: " + umidade + "%");
            System.out.println("Velocidade do Vento: " + velocidadeVento + " km/h");
            System.out.println("Pressão Atmosférica: " + pressaoAtmosferica + " mb");
            System.out.println("==========================================");
            
        } catch (Exception e) {
            System.out.println("Erro ao processar os dados meteorológicos: " + e.getMessage());
        }
    }
    
    // Método auxiliar para validação (Melhoria 6)
    private static boolean contemApenasLetrasEEspacos(String texto) {
        return texto.matches("[a-zA-ZÀ-ÿ\\s]+");
    }
}