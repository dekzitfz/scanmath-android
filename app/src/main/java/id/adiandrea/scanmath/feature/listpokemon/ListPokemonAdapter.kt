package id.adiandrea.scanmath.feature.listpokemon

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.adiandrea.scanmath.R
import id.adiandrea.scanmath.databinding.ItemPokemonBinding
import id.adiandrea.scanmath.feature.detailpokemon.DetailPokemonActivity
import id.adiandrea.scanmath.model.api.pokemon.Pokemon
import androidx.paging.PagingDataAdapter

class ListPokemonAdapter:
    PagingDataAdapter<Pokemon, ListPokemonAdapter.PokemonVH>(DataComparator) {

    object DataComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH =
        PokemonVH(ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
    ))

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        with(holder){
            val pokemon = getItem(position)
            Glide.with(this.itemView.context)
                .load(pokemon?.getImage())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(binding.image)
            binding.name.text = pokemon?.name

            binding.root.setOnClickListener {
                holder.itemView.context.startActivity(
                    Intent(holder.itemView.context, DetailPokemonActivity::class.java)
                        .putExtra(DetailPokemonActivity.POKEMON_NAME, pokemon?.name)
                )
            }
        }
    }

    inner class PokemonVH(val binding: ItemPokemonBinding): RecyclerView.ViewHolder(binding.root)

}