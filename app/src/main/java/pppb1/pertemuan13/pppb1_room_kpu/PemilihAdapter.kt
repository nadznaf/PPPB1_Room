package pppb1.pertemuan13.pppb1_room_kpu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilih
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ItemListBinding

class PemilihAdapter (val listPemilih : List<DataPemilih>, private val onDeleteClick: (Int) -> Unit, private val onEditClick: (Int) -> Unit, private val onViewClick: (Int) -> Unit) : RecyclerView.Adapter<PemilihAdapter.ItemListViewHolder>() {
    class ItemListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataPemilih, position: Int,  onDeleteClick: (Int) -> Unit,
                 onEditClick: (Int) -> Unit,
                 onViewClick: (Int) -> Unit) {
            with(binding) {
                itemNomor.text = (position + 1).toString()
                itemNama.text = data.nama_pemilih

                btnEdit.setOnClickListener{ onEditClick(position) }

                btnDelete.setOnClickListener { onDeleteClick(position) }

                btnDetail.setOnClickListener { onViewClick(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding =
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ItemListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPemilih.size
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(listPemilih[position], position,  onDeleteClick, onEditClick, onViewClick)
    }
}