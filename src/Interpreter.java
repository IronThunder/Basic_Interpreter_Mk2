import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

import java.util.ArrayList;

/***
 * Created by Duncan on 8/4/2015.
 */
public class Interpreter {

    private String code;
    ArrayList<String> lines = new ArrayList<String>();

    public Interpreter(String code){
        this.code = code;
        for (String s : this.code.split("\n")){
            this.lines.add(s);
        }
    }


    public String[] breakStr(String line){
        return line.split(" ");
    }

    public String[] findChars(String input){
        return input.split("");
    }

    public String[] findExps(String input){
        return input.split("\\(");
    }

    public String[] breakByExps(String input) {
        String[] broken = breakStr(input);
        ArrayList<String> result = new ArrayList<>();
        for (int part = 0; part < broken.length; part++) {
            if (findChars(broken[part])[0].equals("(")) {
                String exp = broken[part];
                for (int index = part; index < broken.length; index++) {
                    exp += " " + (broken[index]);
                    if (findChars(broken[index])[-1].equals(")")) {
                        break;
                    }
                }
                result.add(exp);
            }
            else {
                result.add(broken[part]);
            }
        }
        String[] finalVal = result.toArray(new String[result.size()]);
        return finalVal;
    }

    public String printVal(String[] brokenStr){
        int startInd = 0;
        int endInd = 0;
        String result = "";
        for (int index=0; index < brokenStr.length; index++){
            if (brokenStr[index].equals("\"") && startInd == 0){
                startInd = index;
            }
            else if (brokenStr[index].equals("\"") && endInd == 0){
                endInd = index;
            }
        }
        for (int index = startInd + 1; index < endInd; index++){
            result += brokenStr[index];
        }
        return result;
    }

    public boolean isIn(String str, String[] list){
        for (int index = 0; index < list.length; index++){
            if (list[index].contains(str)){
                return true;
            }
        }
        return false;
    }

    private String add(String num1, String num2){
        int int1 = Integer.parseInt(num1);
        int int2 = Integer.parseInt(num2);
        int result = int1 + int2;
        String str = String.valueOf(result);
        return str;
    }

    private String evalExp(String expression){
        String[] parts = findExps(expression);
        if (parts.length == 1){
            String[] text = parts[0].split(" ");
            int intResult = 0;
            String strResult = "";
            if (text.length == 1){
                try {
                    intResult = Integer.parseInt(text[0]);
                }
                catch (NumberFormatException intFailure){
                    try {
                        String[] broken = findChars(text[0]);
                        strResult = printVal(broken);
                    }
                    finally {
                        throw new Error("Was not a parsable type");
                    }
                }
            }
            else {
                String[] expressions = findExps(expression);
                int intTotal = 0;
                String strTotal = "";
                boolean add = true;
                boolean multDiv = false;
                boolean mult = true;
                for (int op = 0; op < expressions.length; op++){
                    if (op == 0 || op % 2 == 0){
                        if (add && !multDiv){
                            try {
                                intTotal += Integer.parseInt(parts[op]);
                            }
                            finally {
                                continue;
                            }
                            try {
                                strTotal += parts[op];
                            }
                            finally {
                                continue;
                            }
                        }
                       else if (!add && !multDiv) {
                           try {
                               intTotal -= Integer.parseInt(parts[op]);
                           }
                           finally {
                               continue;
                           }
                       }
                       else if (mult && multDiv){
                            try {
                                intTotal *= Integer.parseInt(parts[op]);
                            }
                            finally {
                                continue;
                            }
                        }
                        else if (!mult && multDiv){
                            try {
                                intTotal /= Integer.parseInt(parts[op]);
                            }
                            finally {
                                continue;
                            }
                        }
                        else {
                            throw new Error("Not a valid operator");
                        }
                    }
                    else {
                        if (parts[op].equals("+")){
                           add = true;
                           multDiv = false;
                        }
                        else if (parts[op].equals("-")){
                            add = false;
                            multDiv = false;
                        }
                        else if (parts[op].equals("*")){
                            add = false;
                            multDiv = true;
                            mult = true;
                        }
                        else {
                            add = false;
                            multDiv = true;
                            mult = false;
                        }
                    }
                }


            }
        }

    }


}
