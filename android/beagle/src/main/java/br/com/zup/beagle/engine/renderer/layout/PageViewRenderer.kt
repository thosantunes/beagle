/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.engine.renderer.layout

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.view.BeaglePageView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.pager.PageIndicatorComponent

internal class PageViewRenderer(
    override val component: PageView,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<PageView>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        val container = viewFactory.makeBeagleFlexView(rootView.getContext())

        val viewPager = viewFactory.makeViewPager(rootView.getContext()).apply {
            adapter = PageViewAdapter(rootView, component.pages, viewFactory)
        }

        // this container is needed because this view fill the parent completely
        val containerViewPager =
            viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
                addView(viewPager)
            }
        container.addView(containerViewPager)

        component.pageIndicator?.let {
            val pageIndicatorView = viewRendererFactory.make(it).build(rootView)
            setupPageIndicator(component.pages.size, viewPager, component.pageIndicator)
            container.addView(pageIndicatorView)
        }

        return container
    }

    private fun setupPageIndicator(
        pages: Int,
        viewPager: BeaglePageView,
        pageIndicator: PageIndicatorComponent?
    ) {
        pageIndicator?.initPageView(viewPager)
        pageIndicator?.setCount(pages)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                pageIndicator?.onItemUpdated(position)
            }
        })
    }
}

internal class PageViewAdapter(
    private val rootView: RootView,
    private val pages: List<ServerDrivenComponent>,
    private val viewFactory: ViewFactory
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            it.addServerDrivenComponent(pages[position], rootView)
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = pages.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
