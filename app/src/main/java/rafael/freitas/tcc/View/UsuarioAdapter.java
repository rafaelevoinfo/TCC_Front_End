package rafael.freitas.tcc.View;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import rafael.freitas.tcc.Model.CallbackModel;
import rafael.freitas.tcc.Model.Usuario;
import rafael.freitas.tcc.R;

/**
 * Created by rafae on 13/03/2018.
 */

public class UsuarioAdapter extends ArrayAdapter<Usuario> {
    private int resource;
    private LayoutInflater inflater;
    private Animation aninPressButton;
    private CallbackModel<Usuario> callback;

    public UsuarioAdapter(@NonNull Context context, int resource, List<Usuario> usuarios, CallbackModel<Usuario> callback) {
        super(context, resource, usuarios);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        if (aninPressButton == null) {
            aninPressButton = AnimationUtils.loadAnimation(convertView.getContext(), R.anim.press_button);
        }
        // tenta achar um textview
        View tvNome = convertView.findViewById(R.id.tvNome);
        if (tvNome != null) {
            ((TextView) tvNome).setText(getItem(position).toString());
        }

        final ImageButton ibtn = convertView.findViewById(R.id.ibtnImg);
        ibtn.setImageDrawable(convertView.getContext().getResources().getDrawable(android.R.drawable.ic_menu_delete));
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn.startAnimation(aninPressButton);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null){
                            callback.execute(getItem(position));
                        }
                    }
                }, 250);
            }
        });

        return convertView;
    }
}
