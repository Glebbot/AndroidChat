package com.example.androidchat.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.databinding.ItemContainerChannelBinding;
import com.example.androidchat.listeners.ChannelListener;
import com.example.androidchat.model.Channel;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ChannelViewHolder>{

    private final List<Channel> channels;
    private final ChannelListener channelListener;

    public ChannelsAdapter(List<Channel> channels, ChannelListener channelListener) {
        this.channels = channels;
        this.channelListener=channelListener;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerChannelBinding itemContainerChannelBinding = ItemContainerChannelBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ChannelViewHolder(itemContainerChannelBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        holder.setChannelData(channels.get(position));
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        ItemContainerChannelBinding binding;


        ChannelViewHolder(ItemContainerChannelBinding itemContainerChannelBinding) {
            super(itemContainerChannelBinding.getRoot());
            binding = itemContainerChannelBinding;
        }
        void setChannelData(Channel channel) {
            binding.textName.setText(channel.getName());
            binding.getRoot().setOnClickListener(v -> channelListener.onChannelClicked(channel));
        }
    }

}
