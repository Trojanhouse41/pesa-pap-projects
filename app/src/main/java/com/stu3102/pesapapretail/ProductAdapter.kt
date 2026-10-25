package com.stu3102.pesapapretail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvProductCategory)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvStock: TextView = itemView.findViewById(R.id.tvProductStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvCategory.text = product.category
        holder.tvPrice.text = "KES ${product.price_kes}"
        holder.tvStock.text = "Available: ${product.available}"
        
        val imageUrl = if (!product.image_url.isNullOrEmpty()) {
            product.image_url
        } else {
            getMatchingImageUrl(product.name, product.category)
        }

        holder.ivImage.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount() = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    private fun getMatchingImageUrl(name: String, category: String): String {
        val query = (name + " " + category).lowercase()
        return when {
            query.contains("power bank") || query.contains("powerbank") || query.contains("charger") -> "https://images.unsplash.com/photo-1609592424104-16a73c153b6f?w=500"
            query.contains("phone") || query.contains("iphone") || query.contains("samsung") -> "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500"
            query.contains("laptop") || query.contains("macbook") || query.contains("computer") -> "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500"
            query.contains("shoe") || query.contains("sneaker") || query.contains("nike") -> "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"
            query.contains("headphone") || query.contains("audio") || query.contains("airpods") -> "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"
            query.contains("watch") || query.contains("smartwatch") -> "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"
            query.contains("grocery") || query.contains("food") || query.contains("milk") -> "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500"
            else -> "https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=500"
        }
    }
}