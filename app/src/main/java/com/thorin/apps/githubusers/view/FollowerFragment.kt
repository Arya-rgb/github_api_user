package com.thorin.apps.githubusers.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorin.apps.githubusers.R
import com.thorin.apps.githubusers.model.UserFollower
import com.thorin.apps.githubusers.model.UserItem
import com.thorin.apps.githubusers.viewmodel.FollowerViewModel
import com.thorin.apps.githubusers.viewmodel.ListUserFollowerAdapter
import kotlinx.android.synthetic.main.fragment_follower.*


class FollowerFragment : Fragment() {

    companion object {
        val TAG = FollowerFragment::class.java.simpleName
        const val EXTRA_DETAIL = "extra_detail"
    }

    private val listData: ArrayList<UserFollower> = ArrayList()
    private lateinit var adapter: ListUserFollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListUserFollowerAdapter(listData)
        followerViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowerViewModel::class.java)
        val userFollower = activity?.intent?.getParcelableExtra<UserItem>(EXTRA_DETAIL) as UserItem
        config()

        activity?.applicationContext?.let { followerViewModel.getGitApiFollower(it, userFollower.username.toString()) }
        showLoading(true)

        followerViewModel.getListFollowers().observe(requireActivity(), Observer { listDataUser ->
            if (listDataUser != null) {
                adapter.setData(listDataUser)
                showLoading(false)
            }
        })
    }

    private fun config() {
        recyclerViewFollower.layoutManager = LinearLayoutManager(activity)
        recyclerViewFollower.setHasFixedSize(true)
        recyclerViewFollower.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollower.visibility = View.VISIBLE
        } else {
            progressBarFollower.visibility = View.INVISIBLE
        }
    }
}