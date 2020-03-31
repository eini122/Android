/******************************************************************************
 * This is the IO class that read and write file from device directory
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class IO {
    //constructor
    public IO(){};
    //read function
    public ArrayList IORead(File dir) throws IOException {
        ArrayList<DTO> list = new ArrayList<>();//declare array list
        Scanner scores = null;//using scanner to read file
        try{
            DTO dto;////create DTO Object
            scores = new Scanner(dir);//open the directory
            //read the input while it has new line
            while(scores.hasNextLine()){
                String[] input = scores.nextLine().split("\t");//read the string and split using tab
                dto = new DTO(input[0], input[1], input[2]);//store into object DTO
                list.add(dto);//add to list
            }
            //close the scanner
            scores.close();
            return list;//return list
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return list;
        }
    }
    //function that write the
    public void IOWrite(File dir, ArrayList<DTO> list) throws IOException {
        try {
            //open printwriter and write to path dir
            PrintWriter scores = new PrintWriter(dir);
            String line;
            //write each object at list to txt file
            for (DTO dto : list) {
                line = dto.getName() + "\t" + dto.getScore() + "\t" + dto.getDate();
                scores.println(line);
            }
            scores.close();//close file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}