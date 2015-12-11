package com.holcim.altimetrik.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.holcim.altimetrik.android.model.Contact;
import com.holcim.altimetrik.android.utilities.IPredicate;
import com.holcim.altimetrik.android.utilities.Predicate;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.AccountDao;
import com.holcim.altimetrik.android.model.Prospect;
import com.holcim.altimetrik.android.model.ProspectDao;

public class HolcimUnplannedVisitActivity extends HolcimCustomActivity{

    TextView jhidtitle;
    TextView kecamtitle;
    TextView kecamprospects;
    TextView retailerprospects;
    TextView accName;
    TextView accjhid;
    TextView acckec;
    TextView prospectName;
    TextView prospectkec;
    TextView prospectrefno;

    private boolean teleSale = false;
    private String reason;
    private HolcimCreateVisitPlanAccountAdapter adapterAccounts;
    private HolcimCreateVisitPlanProspectAdapter adapterProspects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestCustomTitle();
        setContentView(R.layout.create_visit_plan);
        super.onCreate(savedInstanceState);

        jhidtitle = (TextView) findViewById(R.id.textView_jhid_title);
        kecamtitle = (TextView) findViewById(R.id.textView_kecamatan_title);
        kecamprospects = (TextView) findViewById(R.id.kecamatan_prospects);
        retailerprospects = (TextView) findViewById(R.id.retailer_prospects);

        final ListView listViewAccounts = (ListView)findViewById(R.id.list_view_accounts);
        final List<Account> accountList = HolcimApp.daoSession.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.Name.notEq(""))
                .orderAsc(AccountDao.Properties.Name)
                .list();
        adapterAccounts = new HolcimCreateVisitPlanAccountAdapter(accountList, this);
        listViewAccounts.setAdapter(adapterAccounts);

        final EditText etSearch = (EditText) findViewById(R.id.edittext_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (etSearch.getText().length() != 0) {
                    List<Account> list = filterAccountList(accountList,
                            etSearch.getText().toString().toLowerCase());
                    adapterAccounts = new HolcimCreateVisitPlanAccountAdapter(list,
                            HolcimUnplannedVisitActivity.this);

                } else {
                    adapterAccounts = new HolcimCreateVisitPlanAccountAdapter(accountList,
                            HolcimUnplannedVisitActivity.this);
                }

                listViewAccounts.setAdapter(adapterAccounts);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final ListView listViewProspects = (ListView)findViewById(R.id.list_view_prospects);
        final List<Prospect> prospectList = HolcimApp.daoSession.getProspectDao().queryBuilder()
                .where(ProspectDao.Properties.Name.notEq(""))
                .orderAsc(ProspectDao.Properties.Name)
                .list();
        adapterProspects = new HolcimCreateVisitPlanProspectAdapter(prospectList, this);
        listViewProspects.setAdapter(adapterProspects);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (etSearch.getText().length() != 0) {
                    List<Prospect> list = filterProspectList(prospectList,
                            etSearch.getText().toString().toLowerCase());
                    adapterProspects = new HolcimCreateVisitPlanProspectAdapter(list,
                            HolcimUnplannedVisitActivity.this);

                } else {
                    adapterProspects = new HolcimCreateVisitPlanProspectAdapter(prospectList,
                            HolcimUnplannedVisitActivity.this);
                }

                listViewProspects.setAdapter(adapterProspects);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView homePage = (ImageView) findViewById(R.id.imageButton_home);

        homePage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HolcimCustomActivity.setOnback(true);
                Intent intent = new Intent(HolcimUnplannedVisitActivity.this,
                        HolcimMainActivity.class);
                startActivity(intent);
            }
        });
        if (getIntent().getExtras() != null) {
            teleSale = getIntent().getExtras().getBoolean("tele_sale");
            reason = getIntent().getExtras().getString("reason");
        }

        if (teleSale) {
            setCustomTitle(getResources().getString(R.string.tele_sale_title));
            kecamprospects.setVisibility(View.INVISIBLE);
            retailerprospects.setVisibility(View.INVISIBLE);
            listViewProspects.setVisibility(View.INVISIBLE);
            LinearLayout linearLayoutProspects = (LinearLayout)findViewById(R.id.linear_layout_header_prospects);
            linearLayoutProspects.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            //params.setMargins(45, 0, 45, 45);
            listViewAccounts.setLayoutParams(params);
        } else {
            setCustomTitle(getResources().getString(R.string.unplanned_visit));
        }
    }

    public class HolcimCreateVisitPlanAccountAdapter extends BaseAdapter {
        Context context;
        private final List<Account> fields;

        public HolcimCreateVisitPlanAccountAdapter(List<Account> fields, Context context) {
            this.context = context;
            this.fields = fields;
        }

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.accounts_column_row, parent, false);
            }
            accName=(TextView) convertView.findViewById(R.id.acc_name);
            accjhid=(TextView) convertView.findViewById(R.id.accjh_id);
            acckec=(TextView) convertView.findViewById(R.id.acckecamatan);
            teleSale = getIntent().getExtras().getBoolean("tele_sale");
            accName.setText(fields.get(position).getName());
            accjhid.setText(fields.get(position).getJhid());
            acckec.setText(fields.get(position).getKecamatan());



            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teleSale) {
                        Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimCreatePreorderActivity.class);
                        intent.putExtra("accountId", fields.get(position).getId());
                        intent.putExtra("isAccount", true);
                        intent.putExtra("tele_sale", true);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimSelectedRetailerActivity.class);
                        intent.putExtra("accountId", fields.get(position).getId());
                        intent.putExtra("isAccount", true);
                        intent.putExtra("tele_sale", false);
                        intent.putExtra("reason", reason);
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }
    }

    public class HolcimCreateVisitPlanProspectAdapter extends BaseAdapter {
        Context context;

        private final List<Prospect> fields;

        public HolcimCreateVisitPlanProspectAdapter(List<Prospect> fields, Context context) {
            this.context = context;
            this.fields = fields;
        }

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.prospects_column_row, parent, false);

            }

            prospectName=(TextView) convertView.findViewById(R.id.prosp_name);
            prospectkec=(TextView) convertView.findViewById(R.id.prospectckecamatan);
            prospectrefno = (TextView) convertView.findViewById(R.id.prospectrefno);
            prospectName.setText(fields.get(position).getName());
            prospectkec.setText(fields.get(position).getKecamatan());
            prospectrefno.setText(fields.get(position).getRefnumber());



            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (teleSale) {
                        Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimCreatePreorderActivity.class);
                        intent.putExtra("prospectId", fields.get(position).getId());
                        intent.putExtra("isAccount", false);
                        intent.putExtra("tele_sale", true);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimSelectedRetailerActivity.class);
                        intent.putExtra("prospectId", fields.get(position).getId());
                        intent.putExtra("tele_sale", false);
                        intent.putExtra("isAccount", false);
                        intent.putExtra("reason", reason);
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }
    }
    public static List<Account> filterAccountList(List<Account> list,
                                                  final String filter) {
        List<Account> filteredList = (ArrayList<Account>) Predicate.filter(
                list, new IPredicate<Account>() {
                    public boolean apply(Account detail) {
                        Predicate.predicateParams = detail;
                        boolean ret = false;

                        ret = ((detail.getName() != null && detail
                                .getName().toLowerCase()
                                .contains(filter.toLowerCase()) || (detail.getJhid() != null && detail
                                .getJhid().toLowerCase()
                                .contains(filter.toLowerCase()))));
                        return ret;
                    }
                });
        if (filteredList == null) {
            filteredList = new ArrayList<Account>();
        }
        return filteredList;
    }

    public static List<Prospect> filterProspectList(List<Prospect> list,
                                                    final String filter) {
        List<Prospect> filteredList = (ArrayList<Prospect>) Predicate.filter(
                list, new IPredicate<Prospect>() {
                    public boolean apply(Prospect detail) {
                        Predicate.predicateParams = detail;
                        boolean ret = false;

                        ret = ((detail.getName() != null && detail
                                .getName().toLowerCase()
                                .contains(filter.toLowerCase())) || (detail.getKecamatan() != null && detail
                                .getKecamatan().toLowerCase()
                                .contains(filter.toLowerCase())));
                        return ret;
                    }
                });
        if (filteredList == null) {
            filteredList = new ArrayList<Prospect>();
        }
        return filteredList;
    }
    @Override
    public void onBackPressed() {
        if(!HolcimCustomActivity.blockback){
            HolcimCustomActivity.setOnback(true);
            if (teleSale) {
                Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimMainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HolcimUnplannedVisitActivity.this, HolcimMyVisitPlanListActivity.class);
                startActivity(intent);
            }
            super.onBackPressed();
        }
    }
}
