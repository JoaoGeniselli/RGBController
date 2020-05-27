int pinRed = 9;
int pinGreen = 10;
int pinBlue = 11;

void setup(){
  Serial.begin(9600);
  pinMode(pinRed, OUTPUT);
  pinMode(pinBlue, OUTPUT);
  pinMode(pinGreen, OUTPUT);
}

void loop (){
  if(Serial.available() > 0) {
    String data = Serial.readString();
    Serial.print(data);
    Serial.print("\n");

    int red = data.substring(0, 2).toInt();
    int green = data.substring(4, 6).toInt();
    int blue = data.substring(8, 10).toInt();

    analogWrite(pinRed, red);
    analogWrite(pinGreen, green);
    analogWrite(pinBlue, blue);
  }
}
