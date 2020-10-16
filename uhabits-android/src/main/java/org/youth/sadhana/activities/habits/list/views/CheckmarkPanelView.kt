/*
 * Copyright (C) 2016 Alinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.youth.sadhana.activities.habits.list.views

import android.content.*
import com.google.auto.factory.*
import org.youth.androidbase.activities.*
import org.youth.sadhana.core.models.*
import org.youth.sadhana.core.models.Checkmark.*
import org.youth.sadhana.core.preferences.*
import org.youth.sadhana.core.utils.*

@AutoFactory
class CheckmarkPanelView(
        @Provided @ActivityContext context: Context,
        @Provided preferences: Preferences,
        @Provided private val buttonFactory: CheckmarkButtonViewFactory
) : ButtonPanelView<CheckmarkButtonView>(context, preferences) {

    var values = IntArray(0)
        set(values) {
            field = values
            setupButtons()
        }

    var remarks = Array<String>(0,{ "$it" })
        set(remarks) {
            field = remarks
            setupButtons()
        }

    var color = 0
        set(value) {
            field = value
            setupButtons()
        }

    var onToggle: (Timestamp,Boolean) -> Unit = {timestamp,withRemark ->  }
        set(value) {
            field = value
            setupButtons()
        }

    var onEdit: (Timestamp) -> Unit = {}
        set(value) {
            field = value
            setupButtons()
        }

    override fun createButton(): CheckmarkButtonView = buttonFactory.create()

    @Synchronized
    override fun setupButtons() {
        val today = DateUtils.getToday()

        buttons.forEachIndexed { index, button ->
            val timestamp = today.minus(index + dataOffset)
            button.value = when {
                index + dataOffset < values.size -> values[index + dataOffset]
                else -> UNCHECKED
            }
            button.remark = when {
                index + dataOffset < remarks.size -> remarks[index + dataOffset]
                else -> ""
            }
            button.color = color
            button.onToggle = { withRemark -> onToggle(timestamp, withRemark) }
            button.onEdit = { onEdit(timestamp) }
        }
    }
}
