<?php

namespace App\Database\Migrations;

use CodeIgniter\Database\Migration;

class Predios extends Migration
{
    public function up()
    {
        $this->forge->addField([
            'id' => [
                'type' => 'INT',
                'auto_increment' => true
            ],
            'direccion' => [
                'type' => 'VARCHAR',
                'constraint' => '120'
            ],
            'nroMedidor' => [
                'type' => 'VARCHAR',
                'constraint' => '75'
            ],
            'contactoNombre' => [
                'type' => 'VARCHAR',
                'constraint' => '75'
            ],
            'contactoApellido' => [
                'type' => 'VARCHAR',
                'constraint' => '90'
            ],
            'dni' => [
                'type' => 'VARCHAR',
                'constraint' => '8'
            ],
            'ubigeo' => [
                'type' => 'VARCHAR',
                'constraint' => '250'
            ],
            'latitud' => [
                'type' => 'VARCHAR',
                'constraint' => '120'
            ],
            'longitud' => [
                'type' => 'VARCHAR',
                'constraint' => '120'
            ],            
                        
            'created_at datetime default current_timestamp',
            'updated_at datetime default current_timestamp on update current_timestamp'
        ]);

        $this->forge->addKey('id', true);
        $this->forge->createTable('predios');
    }

    public function down()
    {
        $this->forge->dropTable('predios');
    }
}
