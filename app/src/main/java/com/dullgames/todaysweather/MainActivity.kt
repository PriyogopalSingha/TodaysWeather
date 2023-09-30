package com.dullgames.todaysweather

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dullgames.todaysweather.databinding.ActivityMainBinding
import com.dullgames.todaysweather.models.CityWeather
import com.dullgames.todaysweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val viewModel: WeatherViewModel by viewModels()
    private var city_name: String = "Delhi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        setUpSearch()
        fetchData()
    }

    private fun fetchData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.weatherFlow.collect{
                    when(it){
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            setUpViews(it.data)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT)
                        }
                        else ->{

                        }
                    }
                }
            }
        }

    }

    fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
    private fun setUpSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    viewModel.getWeatherDetails(query)
                    city_name = query
                    fetchData()
                    binding.root.hideKeyboard()
                    binding.searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }



    private fun setUpViews(data: CityWeather?) {
        data?.let{
            val temperature = data.main.temp.toString()
            val max_temp = data.main.temp_max.toString()
            val min_temp = data.main.temp_min.toString()
            val humidity = data.main.humidity.toString()
            val wind_speed = data.wind.speed.toString()
            val sun_rise = data.sys.sunrise.toLong()
            val sun_set = data.sys.sunset.toLong()
            val sea = data.main.pressure.toString()
            val conditions = data.weather.firstOrNull()?.main?: "unknown"

            binding.locationTxtView.text = city_name
            binding.tempTxtView.text = "$temperature°C"
            binding.maxTempTextView.text = "Max: $max_temp °C"
            binding.minTempTxtView.text = "Min: $min_temp °C"
            binding.humidityTxtView.text = "$humidity %"
            binding.sunsetTxtView.text = "${getTime(sun_set)}"
            binding.sunsireTxtView.text = "${getTime(sun_rise)}"
            binding.windTxtView.text = "$wind_speed m/s"
            binding.seaTxtView.text = "$sea hPa"
            binding.conditionsTxtView.text = conditions
            binding.weatherTxtView.text = conditions
            binding.dayTxtView.text = getDay(System.currentTimeMillis())
            binding.dateTxtView.text = getDate(System.currentTimeMillis())
            changeBackgrounds(conditions)
        }

    }

    private fun changeBackgrounds(conditions: String) {
        when(conditions){
            "Clear Sky","Clear" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.clear_sky)
                binding.lottieAnimationView.setAnimation(R.raw.sky)

            }
            "Sunny" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)

            }
            "Partly Clouds", "Clouds", "Overcast" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Haze", "Mist", "Foggy" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.haze_)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Heavy Snow", "Moderate Snow", "Blizzard" -> {
                binding.mainLayout.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else -> {
                binding.mainLayout.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun getDate(time: Long): CharSequence? {
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        return sdf.format(Date(time))
    }

    private fun getDay(time: Long): CharSequence? {
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(Date(time))
    }
    private fun getTime(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date(time*1000))
    }
}