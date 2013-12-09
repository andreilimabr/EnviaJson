package br.com.andreilima.json;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import br.com.andreilima.operadora.modelo.Venda;

import com.google.gson.Gson;

public class EnviaJson {
	public static void main(String[] args) {
		System.out.println("Executando vendas virtuais(Ctrl+c Para Sair)...");
		/**
		 * http://stackoverflow.com/questions/14454063/how-to-make-a-timer
		 */
		final String vendedor =args[0];
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				doRequest("http://localhost:8080/Operadora/realizavenda", vendaAleatoria(vendedor));
				// doRequest("http://ec2-54-207-33-7.sa-east-1.compute.amazonaws.com:8080/Operadora/realizavenda", vendaAleatoria(vendedor));
			}
		}, 0, 30000);
		
	}

	public static void doRequest(String urlRequest, Venda venda) {
		try {
			//URL url = new URL("http://10.10.10.2:8080/ServidorAlunos/MinhaServlet");
			URL url = new URL(urlRequest);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Accept-Charset", "iso-8859-1");
			
		    Gson gson = new Gson();
		   		    
		    String json = gson.toJson(venda);
		    
			conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			//"{\"list\":[{\"aluno\":[{\"nome\":\"Andre\",\"nota\":5},{\"nome\":\"Andrei\",\"nota\":4}]}]}"
			//"{\"venda\":[{\"timestamp\":\"02/12/2013 15:30:45\",\"vendedor\":\"Andrei\",\"loja\":\"1\",\"produto\":\"notebook\",\"qtde\":1.0,\"preco\":10.5,\"regiao\":\"SE\",\"cidade\":\"catanduva\",\"uf\":\"SP\"}]}"
			wr.writeBytes(json);
			wr.flush();
			wr.close();
			int code = conn.getResponseCode();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static Venda vendaAleatoria( String vendedor) {
		final String[] cidades = new String[]{
				"catanduva","são josé do rio preto","marília","são paulo",
				"uberaba","patos de minas","uberlândia","tres corações",
				"salvador","vitória da conquista","campina grande","teresina",
				"manaus","belém","rio branco","palmas",
				"curitiba","florianópolis","blumenau","porto alegre"
		};
		
		final String[] geocode = new String[]{
				"-21.139539,-48.975871","-20.812637,-49.381348","-22.214933,-49.951646","-23.567387,-46.570383",
				"-19.713535,-47.983625","-18.592571,-46.515916","-18.918999,-48.277950","-21.695966,-45.254937",
				"-13.014772,-38.488061","-14.848005,-40.839810","-7.221497,-35.883859","-5.086342,-42.805270",
				"-3.134691,-60.023335","-1.459845,-48.487826","-9.978299,-67.810529","-10.163253,-48.351044",
				"-25.432956,-49.271848","-27.587796,-48.547637","-26.916108,-49.057631","-30.030037,-51.228660"
		};
		
		final int[] lojas_max = new int[]{
				10,10,1,5,
				3,1,2,1,
				3,2,2,2,
				3,2,1,2,
				4,5,2,3
		};
		final String[] estados = new String[]{
				"sp","sp","sp","sp",
				"mg","mg","mg","mg",
				"ba","ba","pb","pi",
				"am","pa","ac","to",
				"pr","sc","sc","rs"
		};
		final String[] regioes = new String[]{
				"se","se","se","se",
				"se","se","se","se",
				"ne","ne","ne","ne",
				"n","n","n","n",
				"s","s","s","s"
		};
		final String[] produtos = new String[]{
				"notebook","no-break","desktop","monitor",
				"tablet","pen-drive","hd externo","celular",
				"game","acessorio","teclado","netbook",
				"placa mae","placa grafica","software","mouse",
				"case desktop","cooler","roteador","datashow"
		};
		
		final double[] preco_min = new double[]{
				700.00,300.00,800.00,300.00,
				299.00,10.00,300.00,199.00,
				50.00,1.00,30.00,700.00,
				500.00,900.00,300.00,10.00,
				80.00,50,00,100.00,1500.00
		};
		
		final double[] preco_max = new double[]{
				7000.00,3000.00,9000.00,5000.00,
				3000.00,125.00,1000.00,1800.00,
				2000.00,6000.00,80.00,1300.00,
				4000.00,3000.00,2500.00,60.00,
				2000.00,125.00,1200.00,8000.00
		};
		
		int posicao = geraNumero(0,19);		
		Venda venda = new Venda();
		venda.setVendedor(vendedor);
		venda.setCidade(cidades[posicao].toUpperCase());
		venda.setGeocode(geocode[posicao]);
		venda.setLoja(String.valueOf(geraNumero(1,lojas_max[posicao])));
		venda.setUf(estados[posicao].toUpperCase());
		venda.setRegiao(regioes[posicao].toUpperCase());
		posicao = geraNumero(0,19);
		venda.setProduto(produtos[posicao]);
		venda.setQtde(geraNumero(1, 5));
		venda.setPreco(geraNumero(preco_min[posicao],preco_max[posicao]));
		venda.setTimestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
		
		return venda;
		
	}

	public static int geraNumero(int min,int max ) {
		/**
		 * http://stackoverflow.com/questions/363681/generating-random-numbers-in-a-range-with-java
		 */
		Random random = new Random();
		int posicao = random.nextInt((max-min)+1) + min;
		return posicao;
	}
	public static double geraNumero(double min,double max ) {
		/**
		 * http://stackoverflow.com/questions/363681/generating-random-numbers-in-a-range-with-java
		 */
		Random random = new Random();
		double posicao = min + (random.nextDouble() * (max-min));
		/**
		 * http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
		 * resp. por MetroidFan2002
		 */
		BigDecimal scale = BigDecimal.valueOf(posicao).setScale(2, BigDecimal.ROUND_HALF_UP);
		return scale.doubleValue();
	}
}
