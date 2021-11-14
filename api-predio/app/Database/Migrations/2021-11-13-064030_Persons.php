<?php

namespace App\Database\Migrations;

use CodeIgniter\Database\Migration;

class Persons extends Migration
{
    public function up()
    {
        $this->forge->addField([
            'id' => [
                'type' => 'INT',
                'auto_increment' => true
            ],
            'correo' => [
                'type' => 'VARCHAR',
                'constraint' => '75'
            ],
            'password' => [
                'type' => 'TEXT',
            ],
            'nombres' => [
                'type' => 'VARCHAR',
                'constraint' => '75'
            ],
            'apellidoPaterno' => [
                'type' => 'VARCHAR',
                'constraint' => '45'
            ],
            'apellidoMaterno' => [
                'type' => 'VARCHAR',
                'constraint' => '45'
            ],
            'perfil' => [
                'type' => 'VARCHAR',
                'constraint' => '25'
            ],
            'foto' => [
                'type' => 'TEXT',
            ],
            'estado' => [
                'type' => 'INT'
            ],
            'idJefe' => [
                'type' => 'INT',
            ],
            'rol_id' => [
                'type' => 'INT',
            ],                        
            'created_at datetime default current_timestamp',
            'updated_at datetime default current_timestamp on update current_timestamp'
        ]);

        $this->forge->addKey('id', true);
        $this->forge->createTable('personas');
    }

    public function down()
    {
        $this->forge->dropTable('personas');
    }
}
