package org.happysanta.messenger;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.happysanta.messenger.messages.ChatFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ChatFragment())
                    .commit();
        }
    }
/*
*
* я создал репозиторий у себя, а ты его склонил себе.
* также я дал тебе права доступа на изменение репозитория
* то есть ты можешь сейчас все это сохранить
*
* и вот слева видишь зел\ное? это значит, что ты тут что-то новенькое вн\с
*збс же, да?
*
* Удобно.
* Теперь я добавлю сюда и Артема. Но он так себе старается, посмотрим что из него выйдет)
* */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. ты понял что я сделал сейчас?

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
