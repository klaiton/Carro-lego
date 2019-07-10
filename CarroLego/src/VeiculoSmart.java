import lejos.utility.Delay;
import java.lang.Math;

public class VeiculoSmart extends Veiculo{

public int distanciaSensor = 0;

public float ateLinha = 0;

/**
 * construtor para definir quais sensores estarao ativos
 * 
 *@param toque : boolean ativa sensor de toque
 *@param colorDir : boolean ativa sensor de cor Direito
 * @param colorDir : boolean ativa sensor de cor Esquerdo
 * @param infravermelho : boolean ativa sensor infravermelho
 */
	
public VeiculoSmart(boolean toque, boolean colorDir, boolean colorEsq, boolean infravermelho) {
	super(toque,colorDir,colorEsq,infravermelho);	
}

/**
 * metodos getters e setters das variaveis distanciaSensor e ateLinha
 * @return
 */

public int getDistanciaSensor() {
	return distanciaSensor;
}

public void setDistanciaSensor(int distanciaSensor) {
	this.distanciaSensor = distanciaSensor;
}

public float getAteLinha() {
	return ateLinha;
}

public void setAteLinha(float ateLinha) {
	this.ateLinha = ateLinha;
}

/**
 * enum para usar lado esquerdo e direito como numeros 
 * DIREITO relaciona 1 e ESQUERDO relaciona 2
 */

public enum Lado{
	DIREITO(1),ESQUERDO(2);
	private int codigo;
  	
	private Lado(int codLado) {  
	        	this.codigo = codLado;
	  	}
	  	public int getLado() {
	        	return this.codigo;
	  	}
}

/**
 * curva para direita, reescrito do metodo curvaDireita 
 * da classe herdada (Veiculo)
 */
public void curvaDireita() {
	this.desligaSincronizacaoEsteiras();
	this.dir.ligaTras();
	this.esq.ligaFrente();
	
}

/**
 * curva para esquerda, reescrito do metodo curvaDireita 
 * da classe herdada (Veiculo)
 */
public void curvaEsquerda() {
	this.desligaSincronizacaoEsteiras();
	this.dir.ligaFrente();
	this.esq.ligaTras();
	
}

/**
 * metodo que percorre em cima da linha preta
 * @param ladoDireito : indentifica o sensor direito de cor 
 * @param ladoEsquerdo : indentifica o sensor esquerdo de cor 
 */

public void  segueLinha(String ladoDireito, String ladoEsquerdo){
	
	super.setVelocidadeEsteirasGrau(250);
	
////anda at� a linha preta
	if(!this.isPreto(ladoDireito) && !this.isPreto(ladoEsquerdo)) {
		this.setEsteirasForward();
		this.curvaDireita();
		while(!this.isPreto(ladoDireito) && !this.isPreto(ladoEsquerdo) && super.getDistancia()> this.getDistanciaSensor());
		this.stop();
	}

////verifica se o sensor de cor da direta esta no preto se sim, curva pra direita at� o sensor
///esquerdo estiver no preto
	if(this.isPreto(ladoDireito) && !this.isPreto(ladoEsquerdo)) {
		this.curvaDireita();
		while(!this.isPreto(ladoEsquerdo)&& super.getDistancia()>  this.getDistanciaSensor());
		this.stop();
	}

////verifica se o sensor de cor da esquerda esta no preto se sim curva pra esquerda at� o sensor
///direito estiver no preto
	if(this.isPreto(ladoEsquerdo) && !this.isPreto(ladoDireito)) {
		this.curvaEsquerda();
		while(!this.isPreto(ladoDireito) && super.getDistancia()>  this.getDistanciaSensor());
		this.stop();
		
	}


	if(this.isPreto(ladoDireito) &&  this.isPreto(ladoDireito)) {
		this.setEsteirasForward();
		while(this.isPreto(ladoDireito) && this.isPreto(ladoEsquerdo) && super.getDistancia()>  this.getDistanciaSensor());
		this.stop();
	}		
			
	
}

/**
 * metodo que abre a garra e depois de um daley fecha a garra
 * 
 */

public void seguraBola() {
	
	super.abreGarra();
	super.fechaGarra();
}

/**
 * metodo que abre a garra totalmente e fecha em seguida
 */

public void largaBola() {
	super.abreGarra();
	Delay.msDelay(1000);
	super.fechaGarra();
}

/**
 * metodo que procura a linha preta
 * quando estiver os dois sensores em cima da linha o carrinho para
 * 
 */
public void linhainicio() {
	this.setEsteirasForward();
	this.setDistanciaSensor(super.getDistancia() -2);  /// variavel distanciaSensor recebe a distancia do sensor at� o chao
	this.setAteLinha(this.dir.getTacometro()); 
	while(!this.isPreto("direito") || !this.isPreto("esquerdo"));
	this.stop();
	
}

/**
 * metodo reescrito da classe herdada
 * nesse metodo foi reesccrito o Delay.msDelay(segundos*1000)
 */

public void setEsteirasForward(int segundos)
{
	this.dir.ligaFrente();
	this.esq.ligaFrente();	
	Delay.msDelay(segundos*500);
	this.stop();
}

/*
 * metodo que inicia o metodo linhainicio() e sugue a linha preta
 * at� o sensor infravermelho indentificar a bolinha
 */

public void pegaBolaNaLinha() {
	
	
	this.linhainicio();
	
/// distancia tem que ser menor que distancia do sensor a bolinha 	
	while(super.getDistancia() > this.getDistanciaSensor() ) {
		this.segueLinha("direito","esquerdo");
	}
	this.stop();
	super.setVelocidadeEsteirasGrau(150);
	super.abreGarra();
///com a bolinha em baixo do sensor, 	
	this.setEsteirasForward(1); /// o carrinho anda por 500ms
	this.seguraBola(); 
	
}

/**
 * o nome do metodo ja fala volta ao ponto de partida
 */

public void voltaAoPontoDeOrigem() {
	super.curvaEsquerda(2); 
	this.stop();
	super.setVelocidadeEsteirasGrau(250);
	this.setEsteirasForward((int) (this.getAteLinha())/360); // anda a distancia at� a faixa
}


}
