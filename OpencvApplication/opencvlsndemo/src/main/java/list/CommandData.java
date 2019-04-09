package list;

import android.app.VoiceInteractor;
import android.app.VoiceInteractor.CommandRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.OAEPParameterSpec;
import javax.sql.CommonDataSource;

@RequiresApi(api = Build.VERSION_CODES.M)
public  class CommandData implements CommandConstants {
    private long id;
    private String command;
    private String name;

  public CommandData(String command,long id)
  {
      this.command=command;
      this.id=id;
  }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static List<CommandData> getCommandList()
    {
        List<CommandData> list=new ArrayList<>();
        list.add(new CommandData(TEST_ENV_COMMAND,1));
        list.add(new CommandData(MAT_PIXEL_INVERT_COMMAND,2));
        list.add(new CommandData(BITMAP_PIXEL_INVERT_COMMAND,3));
        list.add(new CommandData(PIXEL_SUBSTRACT_COMMAND,4));
        list.add(new CommandData(PIXEL_ADD_COMMAND,5));
        list.add(new CommandData(ADJUST_CONTRAST_COMMAND,6));
        list.add(new CommandData(IMAGE_CONTAINER_COMMAND,7));
        list.add(new CommandData(SUB_IMAGE_COMMAND,8));
        list.add(new CommandData(BLUR_IMAGE_COMMAND,9));
        list.add(new CommandData(GAUSSIAN_BLUR_COMMAND,10));
        list.add(new CommandData(CUSTOM_BLUR_COMMAND,11));
        list.add(new CommandData(CUSTOM_EDGE_COMMAND,12));
        list.add(new CommandData(CUSTOM_SHARPEN_COMMAND,13));
        list.add(new CommandData(ERODE_COMMAND,14));
        list.add(new CommandData(DILATE_COMMAND,15));
        list.add(new CommandData(OPEN_COMMAND,16));
        list.add(new CommandData(CLOSE_COMMAND,17));
        list.add(new CommandData(MORPH_LINE_COMMAND,18));
        return list;
    };
}
