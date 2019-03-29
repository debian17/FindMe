package ru.debian17.findme.app.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.debian17.findme.R
import ru.debian17.findme.app.ui.auth.AuthFragment
import ru.debian17.findme.app.ui.menu.MenuFragment
import ru.debian17.findme.app.ui.menu.attribute.add.AddAttributeFragment
import ru.debian17.findme.app.ui.menu.attribute.list.AttributesFragment
import ru.debian17.findme.app.ui.menu.route.add.AddRouteFragment
import ru.debian17.findme.app.ui.menu.route.build.BuildRouteFragment
import ru.debian17.findme.app.ui.auth.registration.RegistrationFragment
import java.lang.IllegalArgumentException

class MainNavigator(private val fragmentManager: FragmentManager) {

    private fun getFragment(screenTag: String): Fragment {
        var fragment = fragmentManager.findFragmentByTag(screenTag)
        if (fragment == null) {
            fragment = when (screenTag) {
                AuthFragment.TAG -> AuthFragment.newInstance()
                RegistrationFragment.TAG -> RegistrationFragment.newInstance()
                MenuFragment.TAG -> MenuFragment.newInstance()
                BuildRouteFragment.TAG -> BuildRouteFragment.newInstance()
                AddRouteFragment.TAG -> AddRouteFragment.newInstance()
                AttributesFragment.TAG -> AttributesFragment.newInstance()
                AddAttributeFragment.TAG -> AddAttributeFragment.newInstance()
                else -> throw IllegalArgumentException("Unknown screenTag=$screenTag")
            }
        }
        return fragment
    }

    fun replaceScreen(screenTag: String) {
        val fragment = getFragment(screenTag)
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right)
            .replace(R.id.flMainContainer, fragment, screenTag)
            .commit()
    }

    fun addFragment(screenTag: String) {
        val fragment = getFragment(screenTag)
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
            .add(R.id.flMainContainer, fragment, screenTag)
            .addToBackStack(screenTag)
            .commit()
    }

    fun back() {
        fragmentManager.popBackStack()
    }

}