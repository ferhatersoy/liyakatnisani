package com.example.liyakatnisani.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.liyakatnisani.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityNavigationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun clickRegisterText_navigatesToRegisterActivity() {
        // LoginActivity'deki "Kayıt Ol" TextView'ine tıkla
        // Layout dosyasındaki ID: binding.tvGoToRegister -> tv_go_to_register
        onView(withId(R.id.tv_go_to_register)).perform(click())

        // RegisterActivity'deki bir UI elemanının (örneğin kayıt butonu) göründüğünü doğrula
        // Layout dosyasındaki ID: binding.btnRegister -> btn_register
        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
    }
}
