package com.stu3102.pesapapretail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvOrderAmount)
        val tvStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val tvReceipt: TextView = itemView.findViewById(R.id.tvMpesaReceipt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.tvId.text = "Order #${order.order_id}"
        holder.tvDate.text = "Date: ${order.order_date}"
        holder.tvAmount.text = "Amount: KES ${order.total_amount}"
        holder.tvStatus.text = "Status: ${order.order_status} (${order.payment_status})"
        holder.tvReceipt.text = "M-Pesa Receipt: ${order.mpesa_receipt_number ?: "N/A"}"
    }

    override fun getItemCount() = orders.size
}