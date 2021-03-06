package mmmi.sdu.dk.gamification;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import mmmi.sdu.dk.gamification.utils.DatabaseFacade;


public class MainActivity extends Activity {

      private FirebaseAuth firebaseAuth;
      private EditText editTextEmail;
      private EditText editTextPassword;
      private ProgressDialog progressDialog;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            DatabaseFacade.getInstance().loadAvatars();
            //Firebase
            firebaseAuth = FirebaseAuth.getInstance();
            editTextEmail = (EditText) findViewById(R.id.userText);
            editTextPassword = (EditText) findViewById(R.id.pwdText);
            if (getIntent().getStringExtra("user") != null) {
                  editTextEmail.setText(getIntent().getStringExtra("user"));
            }
            if (getIntent().getStringExtra("pass") != null) {
                  editTextPassword.setText(getIntent().getStringExtra("pass"));
            }
            Button loginButton = (Button) findViewById(R.id.loginButton);
            progressDialog = new ProgressDialog(this);

            //Redirection
            findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        login();
                  }
            });

            findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        signUp();
                  }
            });

            findViewById(R.id.forgetText).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        forgetPwd();
                  }
            });
      }

      private void login() {
            //Firebase
            String email = editTextEmail.getText().toString().trim();
            String password  = editTextPassword.getText().toString().trim();


            //checking if email and passwords are empty
            if(TextUtils.isEmpty(email)){
                  Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                  return;
            }

            if(TextUtils.isEmpty(password)){
                  Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
                  return;
            }

            progressDialog.setMessage("Logging in Please Wait...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                      finish();
                                      startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                }
                                else {
                                      FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                          }
                    });
      }

      private void signUp() {
            Intent i = new Intent(this, SignupActivity.class);
            startActivity(i);
      }

      private void forgetPwd() {
            Intent i = new Intent(this, ForgetPasswordActivity.class);
            startActivity(i);
      }
}
