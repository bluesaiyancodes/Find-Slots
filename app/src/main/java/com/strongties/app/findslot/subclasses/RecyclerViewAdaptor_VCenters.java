package com.strongties.app.findslot.subclasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
import com.strongties.app.findslot.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RecyclerViewAdaptor_VCenters extends RecyclerView.Adapter<RecyclerViewAdaptor_VCenters.ViewHolder> {

    private final ArrayList<VCenter> mData;
    private final Context mcontext;

    public RecyclerViewAdaptor_VCenters(Context mcontext, ArrayList<VCenter> mData) {
        this.mData = mData;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_vcenters, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.centerName.setText(mData.get(position).getName());
        holder.centerAddr.setText(mData.get(position).getAddress());

        holder.book.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://selfregistration.cowin.gov.in/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mcontext.startActivity(intent);
        });

        AtomicReference<Boolean> flag_viewmore = new AtomicReference<>(false);
        holder.item_short_rl.setOnClickListener(view -> {
            if (flag_viewmore.get()) {
                holder.viewmore.setVisibility(View.GONE);
                holder.item_dose_ll.setVisibility(View.GONE);
                flag_viewmore.set(false);
            } else {
                holder.viewmore.setVisibility(View.VISIBLE);
                holder.item_dose_ll.setVisibility(View.VISIBLE);
                flag_viewmore.set(true);
            }
        });

        //set cost
        holder.cost.setText(mData.get(position).getFee_type());

        ArrayList<VSession> sessions = mData.get(position).getSessions();

        //select the first date
        holder.date1.performClick();
        //set agelimit
        if (sessions.get(0).getMin_age_limit().equals("18")) {
            holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
            holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
            holder.age18.setTextColor(mcontext.getColor(R.color.white));
            holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
        } else {
            holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
            holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
            holder.age45.setTextColor(mcontext.getColor(R.color.white));
            holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
        }
        //set capacity
        holder.capacity.setText(sessions.get(0).avl_capacity);
        //set vaccine
        holder.vaccine.setText(sessions.get(0).getVac_type());
        //Make a chips List
        ArrayList<Chip> chipArrayList = new ArrayList<>();
        chipArrayList.add(holder.chip1);
        chipArrayList.add(holder.chip2);
        chipArrayList.add(holder.chip3);
        chipArrayList.add(holder.chip4);
        chipArrayList.add(holder.chip5);
        //format and set slots
        String slots[] = sessions.get(0).getSlots().split(",");
        slots[0] = slots[0].replace("[\"", "");
        slots[slots.length - 1] = slots[slots.length - 1].replace("\"]", "");
        for (int i = 0; i < slots.length; i++) {
            slots[i] = slots[i].replace("\"", "");
            if (slots.length < 5) {
                chipArrayList.get(i).setText(slots[i]);
                chipArrayList.get(i).setVisibility(View.VISIBLE);
            }
        }
        //Set Vaccine doses
        holder.dose1.setText(sessions.get(0).getAvl_dose1());
        holder.dose2.setText(sessions.get(0).getAvl_dose2());



        //Spin the Available Dates
        {
            holder.date1.setText(sessions.get(0).date.split("-")[0]);
            try {
                holder.date2.setText(mData.get(position).getSessions().get(1).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date2.setVisibility(View.GONE);
            }
            try {
                holder.date3.setText(sessions.get(2).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date3.setVisibility(View.GONE);
            }
            try {
                holder.date4.setText(sessions.get(3).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date4.setVisibility(View.GONE);
            }
            try {
                holder.date5.setText(sessions.get(4).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date5.setVisibility(View.GONE);
            }
            try {
                holder.date6.setText(sessions.get(5).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date6.setVisibility(View.GONE);
            }
            try {
                holder.date7.setText(sessions.get(6).date.split("-")[0]);
            } catch (IndexOutOfBoundsException e) {
                holder.date7.setVisibility(View.GONE);
            }
        }

        //Date Selection UI
        {
            holder.date1.setOnClickListener(view -> {
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(0).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(0).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(0).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(0).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(0).getAvl_dose1());
                holder.dose2.setText(sessions.get(0).getAvl_dose2());

            });

            holder.date2.setOnClickListener(view -> {
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(1).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(1).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(1).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(1).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(1).getAvl_dose1());
                holder.dose2.setText(sessions.get(1).getAvl_dose2());

            });

            holder.date3.setOnClickListener(view -> {
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(2).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(2).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(2).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(2).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(2).getAvl_dose1());
                holder.dose2.setText(sessions.get(2).getAvl_dose2());

            });

            holder.date4.setOnClickListener(view -> {
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(3).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(3).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(3).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(3).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(3).getAvl_dose1());
                holder.dose2.setText(sessions.get(3).getAvl_dose2());

            });

            holder.date5.setOnClickListener(view -> {
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(4).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(4).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(4).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(4).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(4).getAvl_dose1());
                holder.dose2.setText(sessions.get(4).getAvl_dose2());

            });

            holder.date6.setOnClickListener(view -> {
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(5).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(5).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(5).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(5).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(5).getAvl_dose1());
                holder.dose2.setText(sessions.get(5).getAvl_dose2());

            });

            holder.date7.setOnClickListener(view -> {
                holder.date7.setBackground(mcontext.getDrawable(R.drawable.date_background));
                holder.date1.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date2.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date3.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date4.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date5.setBackground(mcontext.getDrawable(R.drawable.date_background2));
                holder.date6.setBackground(mcontext.getDrawable(R.drawable.date_background2));

                //set agelimit
                if (sessions.get(6).getMin_age_limit().equals("18")) {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age18.setTextColor(mcontext.getColor(R.color.white));
                    holder.age45.setTextColor(mcontext.getColor(R.color.cardtext));
                } else {
                    holder.age18.setBackground(mcontext.getDrawable(R.drawable.card_background));
                    holder.age45.setBackground(mcontext.getDrawable(R.drawable.date_background));
                    holder.age45.setTextColor(mcontext.getColor(R.color.white));
                    holder.age18.setTextColor(mcontext.getColor(R.color.cardtext));
                }

                //set capacity
                holder.capacity.setText(sessions.get(6).avl_capacity);

                //set vaccine
                holder.vaccine.setText(sessions.get(6).getVac_type());

                //format and set slots
                String[] _slots = sessions.get(6).getSlots().split(",");
                _slots[0] = _slots[0].replace("[\"", "");
                _slots[slots.length - 1] = _slots[_slots.length - 1].replace("\"]", "");
                for (int i = 0; i < _slots.length; i++) {
                    _slots[i] = _slots[i].replace("\"", "");
                    if (_slots.length < 5) {
                        chipArrayList.get(i).setText(_slots[i]);
                        chipArrayList.get(i).setVisibility(View.VISIBLE);
                    }
                }

                //Set Vaccine doses
                holder.dose1.setText(sessions.get(6).getAvl_dose1());
                holder.dose2.setText(sessions.get(6).getAvl_dose2());

            });
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView centerName;
        TextView centerAddr;
        MaterialTextView book;
        TextView cost;
        TextView date1, date2, date3, date4, date5, date6, date7;
        TextView age18, age45;
        TextView capacity;
        TextView vaccine;
        TextView dose1, dose2;

        ChipGroup chipGroup;
        Chip chip1, chip2, chip3, chip4, chip5;


        RelativeLayout item_short_rl;
        LinearLayout item_dose_ll;
        RelativeLayout viewmore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            centerName = itemView.findViewById(R.id.item_vcenter_name);
            centerAddr = itemView.findViewById(R.id.item_vcenter_address);
            book = itemView.findViewById(R.id.btn_book);

            item_short_rl = itemView.findViewById(R.id.search_result_linear_short);
            item_dose_ll = itemView.findViewById(R.id.item_dose_ll);
            viewmore = itemView.findViewById(R.id.item_more_rl);

            cost = itemView.findViewById(R.id.item_cost);

            date1 = itemView.findViewById(R.id.item_day1);
            date2 = itemView.findViewById(R.id.item_day2);
            date3 = itemView.findViewById(R.id.item_day3);
            date4 = itemView.findViewById(R.id.item_day4);
            date5 = itemView.findViewById(R.id.item_day5);
            date6 = itemView.findViewById(R.id.item_day6);
            date7 = itemView.findViewById(R.id.item_day7);

            age18 = itemView.findViewById(R.id.item_age_18);
            age45 = itemView.findViewById(R.id.item_age_45);

            capacity = itemView.findViewById(R.id.item_capacity);
            vaccine = itemView.findViewById(R.id.item_vaccine);

            chipGroup = itemView.findViewById(R.id.chip_group);
            chip1 = itemView.findViewById(R.id.chip1);
            chip2 = itemView.findViewById(R.id.chip2);
            chip3 = itemView.findViewById(R.id.chip3);
            chip4 = itemView.findViewById(R.id.chip4);
            chip5 = itemView.findViewById(R.id.chip5);

            dose1 = itemView.findViewById(R.id.item_dose1);
            dose2 = itemView.findViewById(R.id.item_dose2);


        }
    }
}
