package uk.co.jpmorgan.dailyreporting.util;

import com.google.gson.*;
import uk.co.jpmorgan.dailyreporting.model.Instruction;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InstructionArrayDeserializer implements JsonDeserializer<Instruction[]> {

    @Override
    public Instruction[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray input = json.getAsJsonArray();
        ArrayList<Instruction> instructions = new ArrayList<>();

        for (int i = 0 ; i < input.size() ; i++) {

            JsonObject jsonObject = input.get(i).getAsJsonObject();

            try {
                Instruction tempInstruction = new Instruction(
                        jsonObject.get("entity").getAsString(),
                        jsonObject.get("operation").getAsCharacter(),
                        jsonObject.get("agreedFx").getAsBigDecimal(),
                        jsonObject.get("currency").getAsString(),
                        jsonObject.get("instructionDate").getAsString(),
                        jsonObject.get("settlementDate").getAsString(),
                        jsonObject.get("units").getAsBigDecimal(),
                        jsonObject.get("pricePerUnit").getAsBigDecimal()
                );

                if (tempInstruction.isValidInstruction()){
                    instructions.add(tempInstruction);
                }
            }
            catch(Exception ex){
                System.out.printf("%s is not a properly formatted instruction.%n", jsonObject.toString());
                ex.printStackTrace();
            }
        }
        return instructions.toArray(new Instruction[instructions.size()]);
    }
}
