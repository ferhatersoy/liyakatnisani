package com.example.liyakatnisani.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.liyakatnisani.R
import com.example.liyakatnisani.MainActivity // MainActivity'i import et
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount


@RunWith(AndroidJUnit4::class)
class SubjectsFragmentDisplayTest {

    @Test
    fun launchMainActivity_subjectsFragmentRecyclerViewIsDisplayed() {
        // MainActivity'i başlat.
        // Bu testin doğru çalışması için MainActivity'nin NavGraph'ında
        // SubjectsFragment'ın başlangıç hedefi olması ve Login ekranının
        // bu test senaryosu için bypass edilmiş olması gerekir.
        // (Manifest dosyasını bu test için MainActivity'yi LAUNCHER yapacak şekilde güncelledik)
        ActivityScenario.launch(MainActivity::class.java)

        // SubjectsFragment içindeki RecyclerView'ın göründüğünü doğrula
        // Layout dosyasındaki ID: binding.recyclerViewSubjects -> recycler_view_subjects
        onView(withId(R.id.recyclerViewSubjects)).check(matches(isDisplayed()))

        // Örnek veriler AppDatabase onCreate callback'inde eklendiği için,
        // RecyclerView'da en az bir öğe olduğunu da kontrol edebiliriz.
        // Bu, veritabanının gerçekten veri içerdiğini ve adaptörün çalıştığını doğrular.
        // Not: Verilerin yüklenmesi zaman alabilir, bu yüzden bu kontrol bazen flaky olabilir.
        // Daha sağlam testler için IdlingResource kullanılabilir. Şimdilik basit tutuyoruz.
        // AppDatabase.populateInitialData içinde 2 konu ekleniyor.
        onView(withId(R.id.recyclerViewSubjects)).check(matches(hasMinimumChildCount(1)))
    }
}
