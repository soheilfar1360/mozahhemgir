package art.navastudio.mozahhemgir

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var roleManager: RoleManager
    private lateinit var statusText: TextView
    private lateinit var roleButton: Button
    private lateinit var enabledSwitch: Switch
    private lateinit var rulesContainer: LinearLayout
    private lateinit var blockedCountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        setContentView(buildUi())
    }

    override fun onResume() {
        super.onResume()
        refreshUi()
    }

    private fun buildUi(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(28))
            gravity = Gravity.RIGHT
        }

        val title = TextView(this).apply {
            text = "مزاحم‌گیر"
            textSize = 30f
            setTypeface(typeface, Typeface.BOLD)
        }
        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "فیلتر متن‌باز و کاملاً محلی تماس‌ها بر اساس پیش‌شماره"
            textSize = 15f
            setPadding(0, dp(6), 0, dp(24))
        }
        root.addView(subtitle)

        statusText = TextView(this).apply {
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 0, 0, dp(10))
        }
        root.addView(statusText)

        roleButton = Button(this).apply {
            text = "فعال‌سازی دسترسی فیلتر تماس"
            setOnClickListener { requestCallScreeningRole() }
        }
        root.addView(roleButton, matchWrap())

        enabledSwitch = Switch(this).apply {
            text = "فیلتر کردن تماس‌ها فعال باشد"
            textSize = 16f
            setPadding(0, dp(18), 0, dp(12))
            setOnCheckedChangeListener { _, checked ->
                RuleStore.setEnabled(this@MainActivity, checked)
            }
        }
        root.addView(enabledSwitch)

        blockedCountText = TextView(this).apply {
            textSize = 14f
            setPadding(0, 0, 0, dp(22))
        }
        root.addView(blockedCountText)

        val section = TextView(this).apply {
            text = "پیش‌شماره‌های مسدود"
            textSize = 19f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 0, 0, dp(10))
        }
        root.addView(section)

        rulesContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        root.addView(rulesContainer, matchWrap())

        val inputRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(12), 0, 0)
        }

        val prefixInput = EditText(this).apply {
            hint = "مثلاً 0942"
            inputType = InputType.TYPE_CLASS_PHONE
            textDirection = View.TEXT_DIRECTION_LTR
            gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
        }
        inputRow.addView(prefixInput, LinearLayout.LayoutParams(0, dp(52), 1f))

        val addButton = Button(this).apply {
            text = "افزودن"
            setOnClickListener {
                val value = prefixInput.text.toString()
                if (RuleStore.addPrefix(this@MainActivity, value)) {
                    prefixInput.text.clear()
                    renderRules()
                } else {
                    Toast.makeText(this@MainActivity, "پیش‌شماره باید ایرانی و حداقل ۴ رقم باشد، یا قبلاً اضافه شده", Toast.LENGTH_SHORT).show()
                }
            }
        }
        inputRow.addView(addButton, LinearLayout.LayoutParams(dp(110), dp(52)).apply {
            marginStart = dp(8)
        })
        root.addView(inputRow, matchWrap())

        val note = TextView(this).apply {
            text = "بدون اینترنت • بدون دسترسی به مخاطبین، Call Log، SMS یا موقعیت مکانی\n\nشماره‌هایی مثل +98942… و 0098942… نیز به‌صورت خودکار به 0942… تبدیل می‌شوند. تماس‌های شماره‌های ذخیره‌شده در مخاطبین، بدون اجازهٔ جداگانهٔ Contacts برای سرویس ارسال نمی‌شوند."
            textSize = 13f
            setPadding(0, dp(24), 0, 0)
        }
        root.addView(note)

        return ScrollView(this).apply {
            addView(root)
        }
    }

    private fun refreshUi() {
        val available = roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)
        val held = available && roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

        statusText.text = when {
            !available -> "این دستگاه نقش Call Screening را پشتیبانی نمی‌کند"
            held -> "وضعیت: فعال"
            else -> "وضعیت: نیاز به فعال‌سازی"
        }
        roleButton.isEnabled = available && !held
        roleButton.text = if (held) "دسترسی Call Screening فعال است" else "فعال‌سازی دسترسی فیلتر تماس"

        enabledSwitch.setOnCheckedChangeListener(null)
        enabledSwitch.isChecked = RuleStore.isEnabled(this)
        enabledSwitch.setOnCheckedChangeListener { _, checked -> RuleStore.setEnabled(this, checked) }

        blockedCountText.text = "تعداد تماس‌های مسدودشده توسط اپ: ${RuleStore.blockedCount(this)}"
        renderRules()
    }

    private fun renderRules() {
        rulesContainer.removeAllViews()
        val prefixes = RuleStore.prefixes(this).sorted()

        if (prefixes.isEmpty()) {
            rulesContainer.addView(TextView(this).apply {
                text = "هنوز پیش‌شماره‌ای ثبت نشده است."
                textSize = 14f
            })
            return
        }

        prefixes.forEach { prefix ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dp(4), 0, dp(4))
            }

            val label = TextView(this).apply {
                text = "$prefix…"
                textSize = 18f
                textDirection = View.TEXT_DIRECTION_LTR
                gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
            }
            row.addView(label, LinearLayout.LayoutParams(0, dp(48), 1f))

            val remove = Button(this).apply {
                text = "حذف"
                setOnClickListener {
                    RuleStore.removePrefix(this@MainActivity, prefix)
                    renderRules()
                }
            }
            row.addView(remove, LinearLayout.LayoutParams(dp(90), dp(48)))
            rulesContainer.addView(row)
        }
    }

    private fun requestCallScreeningRole() {
        if (!roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            Toast.makeText(this, "Call Screening روی این دستگاه در دسترس نیست", Toast.LENGTH_LONG).show()
            return
        }
        if (roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {
            refreshUi()
            return
        }

        val intent: Intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        startActivityForResult(intent, REQUEST_CALL_SCREENING_ROLE)
    }

    @Deprecated("Deprecated in Android API; retained here to avoid external AndroidX dependencies.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CALL_SCREENING_ROLE) {
            refreshUi()
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun matchWrap() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    companion object {
        private const val REQUEST_CALL_SCREENING_ROLE = 1001
    }
}
