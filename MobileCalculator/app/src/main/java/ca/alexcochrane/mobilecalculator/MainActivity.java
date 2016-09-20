package ca.alexcochrane.mobilecalculator;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView calcOutput;
    ScrollView scrollView;
    double firstNum;
    double secondNum;
    String currentOperator;
    boolean justPutInOperator;
    String calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calc = "";
        justPutInOperator = false;
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        calcOutput = (TextView) findViewById(R.id.calcOutput);
        calcOutput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.clear_box));
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        calcOutput.setText("");
                        calc = "";
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.plusminus:
                    if (calcOutput.getText().toString().contains(getString(R.string.subtract))) {
                        calcOutput.setText(
                                calcOutput.getText().toString().toCharArray(),
                                1,
                                calcOutput.getText().toString().toCharArray().length - 1
                        );
                    } else {
                        calcOutput.setText(
                                getString(R.string.subtract)
                                        .concat(calcOutput.getText().toString()));
                    }
                    calc = calcOutput.getText().toString();
                    justPutInOperator = false;
                    break;
                case R.id.equals:
                    secondNum=Double.valueOf(calcOutput.getText().toString());
                    calcOutput.setText("");
                    calc = calcOutput.getText().toString();
                    doMath();
                    break;
                case R.id.clear:
                    currentOperator = null;
                    justPutInOperator = false;
                    firstNum = 0;
                    secondNum = 0;
                    calcOutput.setText("");
                    calc = calcOutput.getText().toString();
                    break;
                case R.id.point:
                    justPutInOperator = false;
                    String s = ((Button) v).getText().toString();
                    if (calcOutput.getText().equals(getString(R.string.error))
                      ||calcOutput.getText().equals(getString(R.string.you_are_stupid))){
                        calcOutput.setText(getString(R.string.zero).concat(s));
                    } else if (!calcOutput.getText().toString().isEmpty()) {
                        calcOutput.setText(calcOutput.getText().toString().concat(s));
                    } else if (calcOutput.getText().toString().contains(getString(R.string.point))) {
                        break;
                    } else {
                        calcOutput.setText(getString(R.string.zero).concat(s));
                    }
                    calc = calcOutput.getText().toString();
                    break;
                case R.id.backspace:
                    String text = calcOutput.getText().toString();
                    if (text.length()>0) {
                        text = text.substring(0, text.length() - 1);
                        calcOutput.setText(text);
                    }
                    calc = calcOutput.getText().toString();
                    break;
                default:
                    String current = ((Button) v).getText().toString();
                    try {
                        Integer.parseInt(current);
                        if (calcOutput.getText().equals(getString(R.string.error))
                          ||calcOutput.getText().equals(getString(R.string.you_are_stupid))
                          ||justPutInOperator) {
                            calcOutput.setText(current);
                        } else if (!calcOutput.getText().toString().isEmpty()) {
                            calcOutput.setText(calcOutput.getText().toString().concat(current));
                        } else {
                            calcOutput.setText(current);
                        }
                        calc = calcOutput.getText().toString();
                        justPutInOperator = false;
                    }
                    catch (Exception e) {
                        if (firstNum==0) {
                            firstNum = Double.valueOf(calcOutput.getText().toString());
                            calcOutput.setText(getString(R.string.empty));
                        }
                        else {
                            secondNum = Double.valueOf(calcOutput.getText().toString());
                            doMath();
                            firstNum = Double.parseDouble((String)calcOutput.getText());
                            secondNum = 0;
                        }
                        currentOperator = ((Button) v).getText().toString();
                        justPutInOperator = true;
                        calc = calcOutput.getText().toString();
                    }

                    break;
            }

        }
        catch (Exception e) {
            calcOutput.setText(getString(R.string.error));
            calc = calcOutput.getText().toString();
        }
        scrollView.scrollTo(0,scrollView.getBottom());


    }
    public void doMath() {
        boolean iTriedToDivideByZero = false;
        justPutInOperator = false;
        Double output = null;
        switch (currentOperator) {
            case "+":
                output = firstNum + secondNum;
                break;
            case "-":
                output = firstNum - secondNum;
                break;
            case "X":
                output = firstNum * secondNum;
                break;
            case "÷":
                if (secondNum==0) {
                    currentOperator = null;
                    justPutInOperator = false;
                    firstNum = 0;
                    secondNum = 0;
                    iTriedToDivideByZero = true;
                    output = null;
                    break;
                }
                else {
                    output = firstNum / secondNum;
                    break;
                }
        }
        if (output == null) {
            if (iTriedToDivideByZero) {
                calcOutput.setText(getString(R.string.you_are_stupid));
                calc = calcOutput.getText().toString();
            }
            else {
                calcOutput.setText(R.string.error);
                calc = calcOutput.getText().toString();
            }
        }
        else {
            if ((double)output.intValue()==output) {
                calcOutput.setText(String.valueOf(output.intValue()));
                calc = calcOutput.getText().toString();
            }
            else {
                calcOutput.setText(String.valueOf(output));
                calc = calcOutput.getText().toString();
            }
        }
        firstNum = 0;
        secondNum = 0;
        currentOperator="+";
    }
}
