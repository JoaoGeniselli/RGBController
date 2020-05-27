int pinoRed = 9;
int pinoGreen = 10;
int pinoBlue = 11;

int val;

void setup(){
  pinMode(pinoRed, OUTPUT);
  pinMode(pinoBlue, OUTPUT);
  pinMode(pinoGreen, OUTPUT);
}
void loop (){
  for(val = 255; val > 0; val --){ //PARA val IGUAL A 255, ENQUANTO val MAIOR QUE 0, DECREMENTA val
      analogWrite(pinoRed, val); //PINO RECEBE O VALOR
      analogWrite(pinoBlue, 255-val); //PINO RECEBE O VALOR
      analogWrite(pinoGreen, 128-val); //PINO RECEBE O VALOR
      delay (10); //INTERVALO DE 10 MILISSEGUNDOS
  }
  for(val = 0; val < 255; val ++){ //PARA val IGUAL A 0, ENQUANTO val MENOR QUE 255, INCREMENTA val
      analogWrite(pinoRed, val); //PINO RECEBE O VALOR
      analogWrite(pinoBlue, 255-val); //PINO RECEBE O VALOR
      analogWrite(pinoGreen, 128-val); //PINO RECEBE O VALOR
      delay (10); //INTERVALO DE 10 MILISSEGUNDOS
  }
}
