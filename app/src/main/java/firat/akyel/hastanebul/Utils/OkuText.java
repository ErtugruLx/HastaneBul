package firat.akyel.hastanebul.Utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by root on 5/7/17.
 */

public class OkuText {
    Context context;
    private MediaPlayer mediaPlayer;

    public OkuText(Context context){
        this.context = context;
    }

    public void Oynat(String cumle){
        String yeniCumle = cumle.replaceAll(" ","%20");
        mediaPlayer = new MediaPlayer();

        try {
            this.mediaPlayer.setDataSource("https://translate.google.com/translate_tts?ie=UTF-8&q="+yeniCumle+"&tl=tr&client=tw-ob");
            this.mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!this.mediaPlayer.isPlaying()){
            this.mediaPlayer.start();
        }else {
            this.mediaPlayer.pause();
        }
    }
}
