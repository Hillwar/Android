package com.hillwar.contacts


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hillwar.contacts.databinding.ListItemBinding


class ContactAdapter(
    private val context: Context,
    contacts: List<Contact>
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val contacts = contacts.toList()
    class ContactViewHolder(
        root: View
    ) :
        RecyclerView.ViewHolder(root) {
        private var binding: ListItemBinding = ListItemBinding.bind(root)
        val name: TextView = binding.name
        val number: TextView = binding.number
        val sms: ImageView = binding.smsButton
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = contacts[position]
        with(holder) {
            name.text = current.name
            number.text = current.phoneNumber
            name.setOnClickListener {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${current.phoneNumber}")
                    )
                )
            }
            number.setOnClickListener {
                name.callOnClick()
            }
            sms.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.SEND_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                      val message = Intent(
                            Intent.ACTION_SEND,
                            Uri.parse("smsto:${current.phoneNumber}")
                        )
                        message.putExtra("sms_body", "Hello, ${name.text}")
                    context.startActivity(message)
                } else {
                    val toast = Toast.makeText(
                        context, "Нет прав",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        }
    }

}