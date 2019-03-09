package com.petsearch.petsearchtask

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.petsearch.petsearchtask.moviedetail.MovieDetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDetailActivityTest{
    @Rule
    @JvmField
    val rule = ActivityTestRule(MovieDetailActivity::class.java)

    @Test
    fun noInternetConnection(){
        Espresso.onView(withId(R.id.txt_error_text)).
                check(matches(withText(rule.activity.
                        getString(R.string.internet_is_not_connected_please_connect_to_internet_and_try_again))))
    }

}