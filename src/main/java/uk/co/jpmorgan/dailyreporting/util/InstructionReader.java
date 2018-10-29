package uk.co.jpmorgan.dailyreporting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.jpmorgan.dailyreporting.model.Instruction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class InstructionReader {

    public List<Instruction> readInstructions(String inputLocation) {
        InputStream inputSteam = getClass().getClassLoader().getResourceAsStream(inputLocation);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputSteam));
        String instructionJson = reader.lines().collect(Collectors.joining("\n"));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instruction[].class, new InstructionArrayDeserializer());
        Gson gson = gsonBuilder.create();
        try {
            Instruction[] instructions = gson.fromJson(instructionJson, Instruction[].class);
            return Arrays.asList(instructions);
        }
        catch(com.google.gson.JsonSyntaxException ex) {
            System.out.println("JSON input is not in a valid structure.");
            ex.printStackTrace();
            return new ArrayList<Instruction>();
        }
    }
}
