<?php

namespace App\Database\Migrations;

use CodeIgniter\Database\Migration;

class Programaciones extends Migration
{
    public function up()
    {
        $this->forge->addField([
            'id' => [
                'type' => 'INT',
                'auto_increment' => true
            ],
            'idPersona' => [
                'type' => 'INT',
            ],
            'idPredio' => [
                'type' => 'INT',
            ],
            'fecha' => [
                'type' => 'DATE',
            ],
            'foto' => [
                'type' => 'TEXT',
            ],
            'estado' => [
                'type' => 'VARCHAR',
                'constraint' => '25'
            ],
            'latitud' => [
                'type' => 'VARCHAR',
                'constraint' => '100'
            ],  
            'longitud' => [
                'type' => 'VARCHAR',
                'constraint' => '100'
            ], 
            'medicion' => [
                'type' => 'VARCHAR',
                'constraint' => '25'
            ], 
            'comentario' => [
                'type' => 'VARCHAR',
                'constraint' => '120'
            ],
            'hora' => [
                'type' => 'VARCHAR',
                'constraint' => '25'
            ],
            'situacion' => [
                'type' => 'VARCHAR',
                'constraint' => '25'
            ],
                        
            'created_at datetime default current_timestamp',
            'updated_at datetime default current_timestamp on update current_timestamp'
        ]);

        $this->forge->addKey('id', true);
        $this->forge->createTable('programaciones');
    }

    public function down()
    {
        $this->forge->dropTable('programaciones');
    }
}
