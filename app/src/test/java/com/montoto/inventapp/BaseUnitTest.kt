package com.montoto.inventapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner.Silent::class)
abstract class BaseUnitTest {
    @MockK
    protected lateinit var mockApplication: Application

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineScopeRule()

    @Before
    open fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }


    @After
    open fun tearDown() {
    }
}


@ExperimentalCoroutinesApi
class MainCoroutineScopeRule(private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)

    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()

    }

}