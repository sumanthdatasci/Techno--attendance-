package com.techno.attendance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.techno.attendance.R;
import com.techno.attendance.adapters.MembersAdapter;
import com.techno.attendance.models.Member;
import com.techno.attendance.storage.StorageManager;
import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends AppCompatActivity {
    private StorageManager storageManager;
    private EditText etSearch;
    private ListView lvMembers;
    private MembersAdapter adapter;
    private List<Member> members;
    private List<Member> filteredMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        storageManager = new StorageManager(this);
        members = new ArrayList<>();
        filteredMembers = new ArrayList<>();

        initializeViews();
        loadMembers();
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.etSearch);
        lvMembers = findViewById(R.id.lvMembers);
        Button btnAddMember = findViewById(R.id.btnAddMember);

        adapter = new MembersAdapter(this, filteredMembers);
        lvMembers.setAdapter(adapter);
        lvMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member member = filteredMembers.get(position);
            Intent intent = new Intent(MembersActivity.this, MemberDetailsActivity.class);
            intent.putExtra("member_id", member.getMemberId());
            startActivity(intent);
        });

        btnAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditMemberActivity.class);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMembers(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void loadMembers() {
        members = storageManager.getAllMembers();
        filteredMembers.clear();
        filteredMembers.addAll(members);
        adapter.notifyDataSetChanged();
    }

    private void filterMembers(String query) {
        filteredMembers.clear();
        if (query.isEmpty()) {
            filteredMembers.addAll(members);
        } else {
            for (Member member : members) {
                if (member.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                    member.getMemberId().contains(query) ||
                    (member.getMobileNumber() != null && member.getMobileNumber().contains(query))) {
                    filteredMembers.add(member);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }
}
