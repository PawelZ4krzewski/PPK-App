import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ubi.database.UserRepository
import com.example.ubi.fragments.loginFragment.LoginUserViewModel

class LoginUserViewModelFactory(
    private  val repository: UserRepository,
    private val application: Application
): ViewModelProvider.Factory{
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginUserViewModel::class.java)) {
            return LoginUserViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}