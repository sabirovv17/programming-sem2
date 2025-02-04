package com.itmo.lab6.client;
import java.io.*;
import java.util.Stack;

import com.itmo.lab6.common.exceptions.RecursionException;
import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.RequestCode;

public class ScriptExecutor {
    private static Stack<File> stack = new Stack<>();
    public static void executeScript(String args, String login, String password) {
        String[] userCommand = args.contains(" ") ? args.split(" ", 2) : new String[]{args, ""}; 
        try {
            if (userCommand[1].isEmpty()) throw new WrongArgumentException(); 
            File file = new File(userCommand[1]); 
            if (!file.exists()) throw new FileNotFoundException(); 
            if (!file.canRead()) throw new SecurityException(); 
            String currentLine; 
            if (stack.contains(file)) throw new RecursionException();
            stack.push(file);

            try (FileInputStream fis = new FileInputStream(file); 
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bis))){
                    
                while ((currentLine = bufferedReader.readLine()) != null) {
                    try {
                        if (currentLine.contains("insert") || currentLine.contains("remove_lower") || currentLine.contains("replace_if_greater") || currentLine.contains("update")) {
                            String[] argsForPerson = new String[11];
                            for (int i = 0; i < argsForPerson.length; i ++) {
                                String arg;
                                if ((arg = bufferedReader.readLine()) != null) {
                                    argsForPerson[i] = arg;
                                }
                            }   
                            Person person = new Person(argsForPerson); 
                            UDPClient.sendRequest(new Request.RequestBuilder().setCommand(currentLine).setPerson(person).setLogin(login).setPassword(password).setRequestCode(RequestCode.COMMAND).build());
                            System.out.println(UDPClient.getResponse());
                        } else if (currentLine.toLowerCase().contains("execute_script")) {
                            String[] argsForCommand = currentLine.contains(" ") ? currentLine.split(" ", 2) : new String[]{currentLine, ""}; 
                            if (argsForCommand[1].isEmpty()) throw new WrongArgumentException();
                            File fileNew = new File(argsForCommand[1]);
                            if (!fileNew.canRead() || !file.exists()) throw new WrongArgumentException();

                            if (stack.contains(fileNew)) throw new RecursionException();
                            else {
                                executeScript(currentLine, login, password);
                            }
                        }
                        else if (currentLine.trim().equals("")) 
                            continue;
                        else {
                            UDPClient.sendRequest(new Request.RequestBuilder().setCommand(currentLine).setRequestCode(RequestCode.COMMAND).setLogin(login).setPassword(password).build());
                            System.out.println(UDPClient.getResponse());
                        }
                    } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException exception) {
                        System.err.println("cannot create new person with arguments");
                    } catch (RecursionException exception) {
                        System.err.println("recursion detected and was skipped");
                    }
                }    
                
            } catch (IOException exception) {
            } catch (ClassNotFoundException exception) {
            } catch (WrongArgumentException exception) {
            } finally {
                stack.pop();
            }
        } catch(WrongArgumentException exception) {
        } catch(FileNotFoundException exception) {
        } catch(SecurityException exception) {
        } catch(RecursionException exception) {
            System.err.println("recursion detected");
        }
    }
}